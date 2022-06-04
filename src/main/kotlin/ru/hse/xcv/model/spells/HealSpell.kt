package ru.hse.xcv.model.spells

/*
 * Heals the hero with `level * 10` hp.
 */
class HealSpell : Spell {
    override val name = "Heal spell"
    override val description = "Heals"
    override var combination = "HH"
    override var coolDown = 10

    /*
     * Get heal amount.
     */
    fun healAmount(level: Int) = level * 10
}
