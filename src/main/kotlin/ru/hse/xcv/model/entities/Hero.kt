package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.items.Item
import ru.hse.xcv.model.spells.ChainLightningSpell
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.model.spells.HealSpell
import ru.hse.xcv.model.spells.SpellBook
import ru.hse.xcv.model.stats.Experience
import ru.hse.xcv.model.stats.Stats

class Hero(position: Position) : Entity(position) {
    val spellBook: SpellBook = SpellBook()
    val inventory: List<Item> = ArrayList()
    val experience: Experience = Experience()
    val level
        get() = experience.level

    override var direction = Position.create(0, 1)
    override var moveSpeed = 25
    override var stats = Stats(
        power = 5,
        armor = 5,
        maxHealth = 100
    )

    private val statsPerLevel = Stats(
        power = 1,
        armor = 1,
        maxHealth = 20
    )

    init {
        // https://www.youtube.com/watch?v=zTbw-ln-Fb4
        spellBook.addSpell(FireballSpell())
        spellBook.addSpell(ChainLightningSpell())
        spellBook.addSpell(HealSpell())
    }

    fun addExperience(exp: Int) {
        val levels = experience.applyExperience(exp)
        if (levels > 0) {
            stats += statsPerLevel * levels
        }
    }
}
