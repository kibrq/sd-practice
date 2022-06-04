package ru.hse.xcv.model.spells

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.DynamicObject

/*
 * Releases a fireball with auto-guidance which targets the closest enemy.
 */
class FireballSpell : Spell {
    override val name = "Fireball"
    override val description = "Deals damage"
    override var combination = "J"
    override var coolDown = 1

    /*
     * Creates a Fireball object.
     */
    fun createFireball(power: Int, position: Position, direction: Position) = Fireball(power, position, direction)

    /*
     * A DynamicObject representing a moving fireball.
     */
    inner class Fireball(
        power: Int,
        override var position: Position,
        override var direction: Position
    ) : DynamicObject() {
        override var moveSpeed = 20
        val damage = power * 5
    }
}
