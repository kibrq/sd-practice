package ru.hse.xcv.model.spells

class ChainLightningSpell : Spell {
    override val name = "Chain lightning"
    override val description = "Chains lightning"
    override var combination = "HJKL"
    override var coolDown = 5
    val durationMillis: Long = 2000
    val range = 10
}
