package ru.hse.xcv.model.spells.book

import ru.hse.xcv.model.spells.*

/*
 * A WTF mode spell book with all spells and no coolDown.
 */
class WtfSpellBook : SpellBook() {
    private fun Spell.toWtfMode(newCombination: String) = apply {
        coolDown = 0
        combination = newCombination
    }

    override val spells = mutableListOf(
        FireballSpell().toWtfMode("J"),
        ChainLightningSpell().toWtfMode("H"),
        HealSpell().toWtfMode("K"),
        SpeedBoostSpell().toWtfMode("L")
    )
}
