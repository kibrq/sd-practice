package ru.hse.xcv.model.items

import ru.hse.xcv.model.stats.Stats

/*
 * An item that increases hero's armor and max HP.
 */
class Helmet : Item {
    override var name: String = "Helmet"
    override val description: String = "Helmet"
    override val bonusStats: Stats = Stats(armor = 3, maxHealth = 20)
    override val type = ItemType.HELMET
}
