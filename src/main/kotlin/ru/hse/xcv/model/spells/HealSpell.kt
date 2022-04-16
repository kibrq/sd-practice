package ru.hse.xcv.model.spells

class HealSpell : Spell {
    override val name = "Heal spell"
    override val description = "Heals"
    override var combination = "HH"
    override var coolDown = 10

    fun healAmount(level: Int) = level * 10
}
