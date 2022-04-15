package ru.hse.xcv.model.spells

class HealSpell : Spell {
    fun healAmount(level: Int) = level * 10
    override val name = "Heal spell"
    override val combination = "HH"
    override val description = "Heals"
    override val coolDown = 1
}
