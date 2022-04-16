package ru.hse.xcv.model.spells

class ChainLightningSpell : Spell {
    override val name = "Chain lightning"
    override val combination = "HJKL"
    override val description = "Chains lightning"
    override var coolDown = 5
}
