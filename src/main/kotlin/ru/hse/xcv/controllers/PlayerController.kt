package ru.hse.xcv.controllers


import org.hexworks.zircon.api.uievent.KeyCode
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.util.InputManager

val UP = KeyCode.KEY_W
val DOWN = KeyCode.KEY_S
val LEFT = KeyCode.KEY_A
val RIGHT = KeyCode.KEY_D

val MOVEMENT = setOf(UP, DOWN, LEFT, RIGHT)

val INVENTORY = KeyCode.KEY_I

val SPELL_H = KeyCode.KEY_H
val SPELL_J = KeyCode.KEY_J
val SPELL_K = KeyCode.KEY_K
val SPELL_L = KeyCode.KEY_L
val SPELL_SUBMIT = KeyCode.SPACE

val SPELL_CASTING = setOf(SPELL_H, SPELL_J, SPELL_K, SPELL_L, SPELL_SUBMIT)

class PlayerController(
    val hero: Hero,
    val input: InputManager,
    override val eventFactory: EventBus
) : ActionController() {
    override fun action() {
        /*
        if (input.isEmpty()) {
            // eventFactory.fire(NoneEvent(this))
            return
        }

        when(input.poll()) {
             UP    -> eventFactory.fire(GameMoveEvent(hero, Position.create(0, -1), this))
             DOWN  -> eventFactory.fire(GameMoveEvent(hero, Position.create(0, 1), this))
             LEFT  -> eventFactory.fire(GameMoveEvent(hero, Position.create(-1, 0), this))
             RIGHT -> eventFactory.fire(GameMoveEvent(hero, Position.create(1, 0), this))

             SPELL_H -> eventFactory.fire(GameLetterPronounced('h', this))
            
        }
        */
    }

    companion object {
        val SUPPORTED_KEYS = setOf(
            UP, DOWN, LEFT, RIGHT, INVENTORY, SPELL_H, SPELL_J, SPELL_K, SPELL_L, SPELL_SUBMIT
        )
    }
}
