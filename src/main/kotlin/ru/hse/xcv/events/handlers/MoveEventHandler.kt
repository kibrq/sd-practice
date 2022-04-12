package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.world.World

import ru.hse.xcv.util.makeCentered

class MoveEventHandler(
    override val world: World,
) : EventHandler<MoveEvent> {
    override fun handle(event: MoveEvent) {
        val (obj, offset, needMoveWorld) = event
        world.moveObject(obj, obj.position + offset)
        if (needMoveWorld) {
            world.view.makeCentered(obj.position)
        }
    }
}
