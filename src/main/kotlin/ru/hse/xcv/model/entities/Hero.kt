package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.items.Item
import ru.hse.xcv.model.spells.ChainLightningSpell
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.model.spells.HealSpell
import ru.hse.xcv.model.spells.SpellBook
import ru.hse.xcv.model.stats.Experience
import ru.hse.xcv.model.stats.Stats

class Hero(
    position: Position,
    direction: Position
) : Entity(position, direction, defaultMoveSpeed, defaultStats) {
    val spellBook: SpellBook = SpellBook()
    val inventory: List<Item> = ArrayList()
    val experience: Experience = Experience()

    init {
        // https://www.youtube.com/watch?v=zTbw-ln-Fb4
        spellBook.spells.add(FireballSpell())
        spellBook.spells.add(ChainLightningSpell())
        spellBook.spells.add(HealSpell())
    }

    fun addExperience(exp: Int) {
        stats += statsPerLevel * experience.applyExperience(exp)
    }

    companion object {
        const val defaultMoveSpeed = 25
        val defaultStats = Stats(
            power = 5,
            armor = 5,
            maxHealth = 100
        )

        val statsPerLevel = Stats(
            power = 1,
            armor = 1,
            maxHealth = 20
        )
    }
}
