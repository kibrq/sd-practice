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

/*
 * Represents player's hero.
 */
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
    override var stats = Stats(
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

    private fun canBeEquipped(item: Item) = !isDead
        && item in inventory
        && equippedItems.size < maxEquippedItems
        && item.type !in equippedItems.map { it.type }

    private fun canBeUnequipped(item: Item) = !isDead && item in equippedItems

    /*
     * Add experience to hero and update stats.
     */
    fun addExperience(exp: Int) {
        val levels = experience.applyExperience(exp)
        if (levels > 0) {
            stats += statsPerLevel * levels
        }
    }

    /*
     * Try to equip `item`.
     */
    fun equipItem(item: Item): Boolean {
        return if (canBeEquipped(item)) {
            equippedItems.add(item)
            stats += item.bonusStats
            true
        } else false
    }

    /*
     * Try to unequip `item`.
     */
    fun unequipItem(item: Item): Boolean {
        return if (canBeUnequipped(item)) {
            equippedItems.remove(item)
            stats -= item.bonusStats
            true
        } else false
    }
}
