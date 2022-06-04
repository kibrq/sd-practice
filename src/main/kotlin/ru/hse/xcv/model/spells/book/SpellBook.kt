package ru.hse.xcv.model.spells.book

import ru.hse.xcv.model.spells.Spell

/*
 * Encapsulates  list of spells.
 */
abstract class SpellBook {
    protected abstract val spells: MutableList<Spell>

    fun addSpell(spell: Spell) = spells.add(spell)

    fun allSpells() = spells.toList()

    fun prefixSearch(prefix: String): List<Spell> = spells.filter {
        it.combination.startsWith(prefix, ignoreCase = true)
    }

    fun search(name: String): Spell? = spells.firstOrNull {
        it.combination.equals(name, ignoreCase = true)
    }
}
