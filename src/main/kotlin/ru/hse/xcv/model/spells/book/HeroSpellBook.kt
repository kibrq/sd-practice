package ru.hse.xcv.model.spells.book

import ru.hse.xcv.model.spells.Spell

/*
 * A default hero spell book.
 */
class HeroSpellBook : SpellBook() {
    override val spells = mutableListOf<Spell>()
}
