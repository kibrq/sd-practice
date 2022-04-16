package ru.hse.xcv.model.spells

class SpeedBoostSpell : Spell {
    override val name = "Speed boost spell"
    override val combination = "L"
    override val description = "Speeds up hero"
    override var coolDown = 10

    val durationMillis = 2000L
    fun newSpeed(oldSpeed: Int) = oldSpeed * 2
    fun oldSpeed(newSpeed: Int) = newSpeed / 2
}
