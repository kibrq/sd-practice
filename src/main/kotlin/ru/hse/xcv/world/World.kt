package ru.hse.xcv.world


import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf
import kotlinx.coroutines.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

import org.hexworks.cobalt.logging.api.LoggerFactory

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Size

import ru.hse.xcv.model.entities.*
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.FieldTile
import ru.hse.xcv.model.FieldModel
import ru.hse.xcv.view.FieldView
import ru.hse.xcv.view.Graphics

import ru.hse.xcv.controllers.ActionController
import ru.hse.xcv.controllers.ActionControllerFactory

import ru.hse.xcv.util.readRect

class World (
    val model: FieldModel,
    val view:  FieldView,
    private val graphics: Graphics,
    private val controllerFactory: ActionControllerFactory
) {
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
        logger.debug("inited")
    }

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

    fun createObject(obj: DynamicObject, position: Position) {
        val onCurrent = lock.read { 
            model.byPosition(position)
        }
        logger.debug { "${onCurrent.first} ${onCurrent.second}" }
        if (onCurrent.first != FieldTile.FLOOR || onCurrent.second != null)
            return
        logger.debug("Put on Map")

        val controller = controllerFactory.create(obj, this)

        lock.write { 
            obj.position = position
            model.dynamicLayer[position] = obj
            controllers[obj] = controller
            view.setBlockAt(position.toPosition3D(1), graphics.dynamicLayerTransform(obj))
        }
        // runBlocking {launch { while(true) controller.action() }}
        
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

    fun <T: DynamicObject> getObjectsByType(clazz: KClass<T>) = controllers.filter{ clazz.isSuperclassOf(it.key::class) }

    fun start() = runBlocking {

        getObjectsByType(Entity::class).entries.forEach {
            logger.debug ("Debug")
            launch { while(true) { delay(1000 / it.key.moveSpeed.toLong()); it.value.action() }}
        }
    
    }

    fun readNeighbourhood(center: Position, size: Size) = lock.read() {
        val shift = Position.create(size.width/2, size.height/2)
        val rect = Rect.create(center - shift, size)
        Pair(model.staticLayer.readRect(rect), model.dynamicLayer.readRect(rect))
    }

    companion object {
        private val NULL_BLOCK = Block.newBuilder<Tile>()
            .withContent(Tile.empty())
            .withEmptyTile(Tile.empty())
            .build()
    }
}
