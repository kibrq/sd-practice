package ru.hse.xcv.model.spells.book

import ru.hse.xcv.model.spells.*

class WtfSpellBook : SpellBook() {
    private fun Spell.toWtfMode(newCombination: String) = apply {
        coolDown = 0
        combination = newCombination
    }

    override val spells = mutableListOf(
        ChainLightningSpell().toWtfMode("H"),
        FireballSpell().toWtfMode("J"),
        HealSpell().toWtfMode("K"),
        SpeedBoostSpell().toWtfMode("L")
    )
}
