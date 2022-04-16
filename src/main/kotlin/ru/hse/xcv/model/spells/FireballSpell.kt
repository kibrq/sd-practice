package ru.hse.xcv.model.spells

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.DynamicObject

class FireballSpell : Spell {
    override val name = "Fireball"
    override val combination = "J"
    override val description = "Deals damage"
    override var coolDown = 1

    fun createFireball(power: Int, position: Position, direction: Position) = Fireball(power, position, direction)

    inner class Fireball(
        power: Int,
        override var position: Position,
        override var direction: Position
    ) : DynamicObject() {
        override var moveSpeed = 20
        val damage = power * 5
    }
}
