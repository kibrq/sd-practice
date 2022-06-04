package ru.hse.xcv.events.handlers

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.util.addAll
import ru.hse.xcv.util.makeCentered
import ru.hse.xcv.world.World

/*
 * Handles MoveEvent.
 */
class MoveEventHandler(
    override val world: World
) : GameEventHandler<MoveEvent> {
    /*
     * Tries to move `event.obj` by `event.offset`.
     */
    override fun handle(event: MoveEvent) {
        val (obj, offset, needMoveWorld) = event
        val possibleMoves = mutableListOf(
            offset,
            Position.create(offset.x, 0),
            Position.create(0, offset.y)
        )
        if (event.crazyMovements) {
            if (offset.x == 0) {
                possibleMoves.addAll(
                    Position.create(1, offset.y),
                    Position.create(-1, offset.y)
                )
            }
            if (offset.y == 0) {
                possibleMoves.addAll(
                    Position.create(offset.x, 1),
                    Position.create(offset.x, -1)
                )
            }
        }
        for (currentOffset in possibleMoves) {
            if (currentOffset == Position.zero()) {
                continue
            }
            if (world.moveObject(obj, obj.position + currentOffset)) {
                break
            }
        }
        if (needMoveWorld) {
            world.view.makeCentered(obj.position)
        }
    }
}
