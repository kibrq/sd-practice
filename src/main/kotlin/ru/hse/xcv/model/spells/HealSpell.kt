package ru.hse.xcv.model.spells

class HealSpell: Spell {
    override val name: String = "Heal spell"
    override val description: String = "Heals"
    override val coolDown: Int = 1
}
