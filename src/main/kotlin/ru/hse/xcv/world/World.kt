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
import ru.hse.xcv.model.OnMapObject
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.util.debug
import ru.hse.xcv.util.readRect
import ru.hse.xcv.util.straightPathTo
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
        logger.debug(model)
        model.rect.fetchPositions().forEach { position ->
            val (tile, obj) = model.byPosition(position)

            tile?.let {
                view.setBlockAt(position.toPosition3D(0), graphics.staticLayerTransform(it))
            }

            obj?.let {
                view.setBlockAt(position.toPosition3D(1), graphics.dynamicLayerTransform(it))
                if (it is DynamicObject) {
                    val controller = controllerFactory.create(it, this)
                    controllers[it] = controller
                }
            }
        }
        logger.debug("world initialized")
    }

    val hero: Hero
        get() = getAllObjectsOfType(Hero::class).keys.first()

    fun delayed(millis: Long, block: () -> Unit) = scope.launch {
        delay(millis)
        block()
    }

    fun getDynamicLayer(position: Position): OnMapObject? = model.dynamicLayer[position]

    fun getStaticLayer(position: Position): FieldTile? = model.staticLayer[position]

    fun isEmpty(position: Position) =
        model.staticLayer[position] == FieldTile.FLOOR && model.dynamicLayer[position] == null

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

    fun createObject(obj: OnMapObject, position: Position): Boolean {
        val onCurrent = lock.read {
            model.byPosition(position)
        }
        logger.debug { "try create on ${onCurrent.first}:${onCurrent.second}" }

        if (onCurrent.first != FieldTile.FLOOR || onCurrent.second != null)
            return false

        logger.debug("Put $obj on map")

        lock.write {
            obj.position = position
            model.dynamicLayer[position] = obj
            view.setBlockAt(position.toPosition3D(1), graphics.dynamicLayerTransform(obj))
            if (obj is DynamicObject) {
                val controller = controllerFactory.create(obj, this)
                controllers[obj] = controller
                scope.launch {
                    do {
                        delay(5000 / obj.moveSpeed.toLong())
                    } while (controller.action())
                }

            }
        }
        return true
    }

    fun deleteObject(obj: OnMapObject) {
        val onCurrent = lock.read {
            model.byPosition(obj.position)
        }

        if (onCurrent.first != FieldTile.FLOOR || onCurrent.second == null)
            return

        lock.write {
            model.dynamicLayer.remove(obj.position)
            view.setBlockAt(obj.position.toPosition3D(1), NULL_BLOCK)
            if (obj is DynamicObject) {
                controllers.remove(obj)
            }
        }
    }

    fun start() = getAllObjectsOfType(Entity::class).entries.forEach { (entity, controller) ->
        scope.launch {
            do {
                delay(5000 / entity.moveSpeed.toLong())
            } while (!entity.isDead && controller.action())
            if (entity is Hero) {
                logger.debug("Hero is dead")
            }
        }
    }

    fun <T : OnMapObject> nearestVisibleObjectInRectangle(center: Position, size: Size, clazz: KClass<T>): T? =
        readNeighbourhood(center, size).second.values
            .mapNotNull { clazz.safeCast(it) }
            .filter { isVisibleFrom(it.position, center) }
            .minByOrNull {
                val distance = it.position - center
                distance.x * distance.x + distance.y * distance.y
            }

    private fun <T : OnMapObject> getAllObjectsOfType(clazz: KClass<T>): Map<T, ActionController> = lock.read {
        controllers.mapNotNull { (obj, controller) ->
            clazz.safeCast(obj)?.let {
                it to controller
            }
        }.toMap()
    }

    private fun isVisibleFrom(position: Position, other: Position): Boolean = lock.read {
        position.straightPathTo(other).all {
            model.staticLayer[it] == FieldTile.FLOOR
        }
    }

    private fun readNeighbourhood(center: Position, size: Size) = lock.read {
        val shift = Position.create(size.width / 2, size.height / 2)
        val rect = Rect.create(center - shift, size)
        model.staticLayer.readRect(rect) to model.dynamicLayer.readRect(rect)
    }

    companion object {
        private val NULL_BLOCK = Block.newBuilder<Tile>()
            .withContent(Tile.empty())
            .withEmptyTile(Tile.empty())
            .build()
    }
}
