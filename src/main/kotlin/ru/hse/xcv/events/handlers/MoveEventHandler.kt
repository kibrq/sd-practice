package ru.hse.xcv.events.handlers

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.util.makeCentered
import ru.hse.xcv.world.World

class MoveEventHandler(
    override val world: World,
) : EventHandler<MoveEvent> {
    override fun handle(event: MoveEvent) {
        val (obj, offset, needMoveWorld) = event
        val possibleMoves = listOf(
            offset,
            Position.create(offset.x, 0),
            Position.create(0, offset.y)
        )
        for (currentOffset in possibleMoves) {
            val moved = world.moveObject(obj, obj.position + currentOffset)
            if (moved) break
        }
        if (needMoveWorld) {
            world.view.makeCentered(obj.position)
        }
    }
}
