package ru.hse.xcv.model.spells

class SpellBook {
    val spells: List<Spell> = ArrayList()

    fun prefixSearch(prefix: String): List<Spell> {
        return spells.filter { spell -> spell.name.startsWith(prefix, ignoreCase = true) }
    }
}
