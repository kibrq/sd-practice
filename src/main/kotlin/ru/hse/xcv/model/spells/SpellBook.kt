package ru.hse.xcv.model.spells

class SpellBook {
    private val spells = mutableListOf<Spell>()
    private val secretSpell = WtfSpell()

    fun addSpell(spell: Spell) = spells.add(spell)

    fun allSpells() = spells.toList()

    fun prefixSearch(prefix: String): List<Spell> = spells.filter {
        it.combination.startsWith(prefix, ignoreCase = true)
    }

    fun search(name: String): Spell? = if (name == secretSpell.combination) secretSpell else
        spells.firstOrNull {
            it.combination.equals(name, ignoreCase = true)
        }
}
