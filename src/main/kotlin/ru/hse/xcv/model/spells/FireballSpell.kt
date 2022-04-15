package ru.hse.xcv.model.spells

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.DynamicObject

class FireballSpell : Spell {
    override val name = "Fireball"
    override val combination = "JJK"
    override val description = "Deals damage"
    override val coolDown = 1

    fun createFireball(position: Position, level: Int) = Fireball(position, level)

    inner class Fireball(
        position: Position,
        level: Int
    ) : DynamicObject(position, Position.create(0, 0), 15) {
        val damage = level * 10
    }
}
