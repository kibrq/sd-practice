package ru.hse.xcv.world


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.*
import ru.hse.xcv.controllers.ActionController
import ru.hse.xcv.controllers.ActionControllerFactory
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.FieldModel
import ru.hse.xcv.model.FieldTile
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.util.readRect
import ru.hse.xcv.view.FieldView
import ru.hse.xcv.view.Graphics
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

class World(
    val model: FieldModel,
    val view: FieldView,
    private val graphics: Graphics,
    private val controllerFactory: ActionControllerFactory
) {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val lock = ReentrantReadWriteLock()
    private val controllers = hashMapOf<DynamicObject, ActionController>()
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.debug("$model")
        model.rect.fetchPositions().forEach { position ->
            val (tile, obj) = model.byPosition(position)

            if (tile != null) {
                view.setBlockAt(position.toPosition3D(0), graphics.staticLayerTransform(tile))
            }

            if (obj != null) {
                val controller = controllerFactory.create(obj, this)
                controllers[obj] = controller
                view.setBlockAt(position.toPosition3D(1), graphics.dynamicLayerTransform(obj))
            }
        }
        logger.debug("world initialized")
    }

    val hero: Hero = getAllObjectsOfType(Hero::class).keys.first()

    fun isEmpty(position: Position) =
        model.staticLayer[position] == FieldTile.FLOOR && model.dynamicLayer[position] == null

    fun <T : DynamicObject> getAllObjectsOfType(clazz: KClass<T>): Map<T, ActionController> =
        controllers.mapNotNull { (obj, controller) ->
            clazz.safeCast(obj)?.let {
                it to controller
            }
        }.toMap()

    fun moveObject(obj: DynamicObject, newPosition: Position): Boolean {
        val currentPosition = obj.position

        val (onCurrent, onNew, block) = lock.read {
            Triple(
                model.byPosition(currentPosition),
                model.byPosition(newPosition),
                view.blocks[currentPosition.toPosition3D(1)]
            )
        }

        if (onCurrent.first != FieldTile.FLOOR || onCurrent.second !== obj)
            return false
        if (onNew.first != FieldTile.FLOOR || onNew.second != null)
            return false
        if (block == null || block === NULL_BLOCK)
            return false

        lock.write {
            obj.position = newPosition

            model.dynamicLayer.remove(currentPosition)
            model.dynamicLayer[newPosition] = obj

            view.setBlockAt(currentPosition.toPosition3D(1), NULL_BLOCK)
            view.setBlockAt(newPosition.toPosition3D(1), block)
        }

        return true
    }

    fun createObject(obj: DynamicObject, position: Position): Boolean {
        val onCurrent = lock.read {
            model.byPosition(position)
        }
        logger.debug { "try create on ${onCurrent.first}:${onCurrent.second}" }

        if (onCurrent.first != FieldTile.FLOOR || onCurrent.second != null)
            return false

        logger.debug("Put $obj on map")

        val controller = controllerFactory.create(obj, this)

        lock.write {
            obj.position = position
            model.dynamicLayer[position] = obj
            controllers[obj] = controller
            view.setBlockAt(position.toPosition3D(1), graphics.dynamicLayerTransform(obj))
        }

        scope.launch {
            do {
                delay(5000 / obj.moveSpeed.toLong())
            } while (controller.action())
            deleteObject(obj)
        }

        return true
    }

    fun deleteObject(obj: DynamicObject) {
        val onCurrent = lock.read {
            model.byPosition(obj.position)
        }

        if (onCurrent.first != FieldTile.FLOOR || onCurrent.second == null)
            return

        lock.write {
            model.dynamicLayer.remove(obj.position)
            controllers.remove(obj)
            view.setBlockAt(obj.position.toPosition3D(1), NULL_BLOCK)
        }
    }

    fun start() = getAllObjectsOfType(Entity::class).entries.forEach { (entity, controller) ->
        scope.launch {
            do {
                delay(5000 / entity.moveSpeed.toLong())
            } while (controller.action() && !entity.isDead())
            deleteObject(entity)
            if (entity is Hero) {
                logger.debug("Hero is dead")
            }
        }
    }

    fun readNeighbourhood(center: Position, size: Size) = lock.read {
        val shift = Position.create(size.width / 2, size.height / 2)
        val rect = Rect.create(center - shift, size)
        model.staticLayer.readRect(rect) to model.dynamicLayer.readRect(rect)
    }

    fun <T : DynamicObject> nearestObjectInNeighbourhood(center: Position, size: Size, clazz: KClass<T>): T? =
        readNeighbourhood(center, size).second.values
            .mapNotNull { clazz.safeCast(it) }
            .minByOrNull {
                val distance = it.position - center
                distance.x * distance.x + distance.y * distance.y
            }


    companion object {
        private val NULL_BLOCK = Block.newBuilder<Tile>()
            .withContent(Tile.empty())
            .withEmptyTile(Tile.empty())
            .build()
    }
}
