package ru.hse.xcv.model.spells

class HealSpell : Spell {
    override val name = "Heal spell"
    override val combination = "HH"
    override val description = "Heals"
    override val coolDown = 10

    fun healAmount(level: Int) = level * 10
}
