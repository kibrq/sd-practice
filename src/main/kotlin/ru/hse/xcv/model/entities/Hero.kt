package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.items.Item
import ru.hse.xcv.model.spells.ChainLightningSpell
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.model.spells.HealSpell
import ru.hse.xcv.model.spells.SpeedBoostSpell
import ru.hse.xcv.model.spells.book.HeroSpellBook
import ru.hse.xcv.model.stats.Experience
import ru.hse.xcv.model.stats.Stats

class Hero(position: Position) : Entity(position) {
    private var maxEquippedItems: Int = 3
    val spellBook: HeroSpellBook = HeroSpellBook()
    val inventory: MutableList<Item> = mutableListOf()
    val equippedItems: MutableList<Item> = mutableListOf()

    val experience: Experience = Experience()
    val power
        get() = stats.power

    override var direction = Position.create(0, 1)
    override var moveSpeed = 25
    override val stats = Stats(
        power = 3,
        armor = 3,
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
        spellBook.addSpell(SpeedBoostSpell())
    }

    fun addExperience(exp: Int) {
        val levels = experience.applyExperience(exp)
        if (levels > 0) {
            stats += statsPerLevel * levels
        }
    }

    fun equipItem(item: Item): Boolean {
        return if (item in inventory && equippedItems.size < maxEquippedItems) {
            equippedItems.add(item)
            stats += item.bonusStats
            true
        } else false
    }

    fun unequipItem(item: Item): Boolean {
        return if (equippedItems.contains(item)) {
            equippedItems.remove(item)
            stats -= item.bonusStats
            true
        } else false
    }
}
