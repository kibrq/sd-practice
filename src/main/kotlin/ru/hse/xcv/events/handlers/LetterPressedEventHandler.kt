package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.LetterPressedEvent
import ru.hse.xcv.world.World

/*
 * Handles LetterPressedEvent.
 */
class LetterPressedEventHandler(
    override val world: World
) : GameEventHandler<LetterPressedEvent> {
    /*
     * Ruslan had a job to write this letter on the screen.
     */
    override fun handle(event: LetterPressedEvent) {
        event.letter // pls draw HJKL or SPACE
    }
}
