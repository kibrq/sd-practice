package ru.hse.xcv.model.spells

class WtfSpell : Spell {
    override val name = "WTF spell"
    override val combination = "H".repeat(10)
    override val description = "WTF mode on"
    override var coolDown = 0
}
