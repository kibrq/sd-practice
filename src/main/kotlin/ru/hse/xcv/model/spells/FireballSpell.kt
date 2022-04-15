package ru.hse.xcv.model.spells

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.DynamicObject

class FireballSpell : Spell {
    override val name = "Fireball"
    override val combination = "JJK"
    override val description = "Deals damage"
    override val coolDown = 1

    fun createFireball(level: Int, position: Position, direction: Position) = Fireball(level, position, direction)

    inner class Fireball(
        level: Int,
        override var position: Position,
        override var direction: Position
    ) : DynamicObject() {
        override var moveSpeed = 20
        val damage = level * 10
    }
}
