package ru.hse.xcv.model.spells

class FireballSpell : Spell {
    override val name: String = "Fireball"
    override val description: String = "Deals damage"
    override val coolDown: Int = 1
}
