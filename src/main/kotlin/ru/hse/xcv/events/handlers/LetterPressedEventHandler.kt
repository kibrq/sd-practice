package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.LetterPressedEvent
import ru.hse.xcv.world.World

class LetterPressedEventHandler(
    override val world: World
) : GameEventHandler<LetterPressedEvent> {
    override fun handle(event: LetterPressedEvent) {
        event.letter // pls draw HJKL or SPACE
    }
}
