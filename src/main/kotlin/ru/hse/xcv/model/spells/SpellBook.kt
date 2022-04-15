package ru.hse.xcv.model.spells

class SpellBook {
    private val spells = mutableListOf<Spell>()

    fun addSpell(spell: Spell) = spells.add(spell)

    fun prefixSearch(prefix: String): List<Spell> = spells.filter {
        it.combination.startsWith(prefix, ignoreCase = true)
    }

    fun search(name: String): Spell? = spells.firstOrNull {
        it.combination.equals(name, ignoreCase = true)
    }
}
