package ru.hse.xcv.handlers

import ru.hse.xcv.events.GameMoveEvent
import ru.hse.xcv.handlers.GameEventHandler
import ru.hse.xcv.model.Field
import ru.hse.xcv.view.GameWorld


class MoveEventHandler(
    protected override val field: Field, 
    protected override val world: GameWorld,
): GameEventHandler<GameMoveEvent>() {
    override fun handle(event: GameMoveEvent) {
        val (obj, pos, _) = event
        // TODO
    }
}
