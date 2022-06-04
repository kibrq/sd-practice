package ru.hse.xcv.model.spells

/*
 * Doubles the hero speed for 2 seconds.
 */
class SpeedBoostSpell : Spell {
    override val name = "Speed boost spell"
    override val description = "Speeds up hero"
    override var combination = "L"
    override var coolDown = 10

    val durationMillis = 2000L
    fun newSpeed(oldSpeed: Int) = oldSpeed * 2
    fun oldSpeed(newSpeed: Int) = newSpeed / 2
}
