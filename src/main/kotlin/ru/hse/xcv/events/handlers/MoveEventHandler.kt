package ru.hse.xcv.events.handlers

import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.model.Field
import ru.hse.xcv.view.GameWorld

class MoveEventHandler(
    override val field: Field,
    private val world: GameWorld
) : EventHandler<MoveEvent> {
    override fun handle(event: MoveEvent) {
        val pos = event.obj.position.toPosition3D(1)
        val block = world.blocks[pos]
        val kal = Block.newBuilder<Tile>()
            .withContent(Tile.empty())
            .withEmptyTile(Tile.empty())
            .build()
        world.setBlockAt(pos, kal)
        repeat(event.obj.moveSpeed) {
            val newPos = event.obj.position + event.offset
            if (field.dynamicLayer[newPos] == null) {
                field.dynamicLayer.remove(event.obj.position)
                event.obj.position = newPos
                field.dynamicLayer[newPos] = event.obj
                if (event.moveWorld) {
                    world.toCenter(event.obj.position)
                }
            }
        }
        block?.let {
            world.setBlockAt(event.obj.position.toPosition3D(1), it)
        }
    }

    private fun GameWorld.scrollBy(offset: Position) {
        val pos = visibleOffset
        scrollTo((pos.to2DPosition() + offset).toPosition3D(0))
    }

    private fun GameWorld.toCenter(position: Position) {
        val (x, y) = this.visibleSize
        val pos = Position3D.create(maxOf(position.x - x / 2, 0), maxOf(position.y - y / 2, 0), 0)
        scrollTo(pos)
    }
}
