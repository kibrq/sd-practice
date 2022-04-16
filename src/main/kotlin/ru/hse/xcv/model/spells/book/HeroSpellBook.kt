package ru.hse.xcv.model.spells.book

import ru.hse.xcv.model.spells.Spell

class HeroSpellBook: SpellBook() {
    override val spells = mutableListOf<Spell>()
}
