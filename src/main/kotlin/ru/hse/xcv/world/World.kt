package ru.hse.xcv.world


import kotlin.reflect.KClass
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

import org.hexworks.cobalt.logging.api.LoggerFactory

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Size

import ru.hse.xcv.model.Hero
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
        logger.debug("${model}")
        model.rect.fetchPositions().forEach { position ->
            val (tile, obj) = model.byPosition(position)
            
            if (tile != null) {
                view.setBlockAt(position.toPosition3D(0), graphics.staticLayerTransform(tile))
            }

            if (obj != null) {
                val controller = controllerFactory.create(obj, this)
                controllers.put(obj, controller)
                view.setBlockAt(position.toPosition3D(1), graphics.dynamicLayerTransform(obj))
            }
        }
        logger.debug("inited")
    }

    fun moveObject(obj: DynamicObject, newPosition: Position) {
        val currentPosition = obj.position
        
        val (onCurrent, onNew, block) = lock.read { 
            Triple(
                model.byPosition(currentPosition),
                model.byPosition(newPosition),
                view.blocks[currentPosition.toPosition3D(1)]
            )
        }

        if (onCurrent.first != FieldTile.FLOOR || onCurrent.second !== obj)
            return
        if (onNew.first != FieldTile.FLOOR || onNew.second != null)
            return
        if (block == null || block === NULL_BLOCK)
            return
        
        lock.write { 
            obj.position = newPosition
            obj.direction = newPosition - currentPosition
            
            model.dynamicLayer.remove(currentPosition)
            model.dynamicLayer.put(newPosition, obj)

            view.setBlockAt(currentPosition.toPosition3D(1), NULL_BLOCK)
            view.setBlockAt(newPosition.toPosition3D(1), block)
        }
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
            model.dynamicLayer.put(position, obj)
            controllers.put(obj, controller)
            view.setBlockAt(position.toPosition3D(1), graphics.dynamicLayerTransform(obj))
        }

        controller.action()
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

    fun <T: DynamicObject> getObjectsByType(clazz: KClass<T>) = controllers.keys.filter{ clazz.isInstance(it) }.toList()

    fun start() {
        controllers.values.forEach { controller -> controller.start() }
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
