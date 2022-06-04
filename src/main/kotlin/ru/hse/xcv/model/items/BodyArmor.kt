package ru.hse.xcv.model.items

import ru.hse.xcv.model.stats.Stats

/*
 * An item that increases hero's armor and max HP.
 */
class BodyArmor : Item {
    override val name: String = "Body armor"
    override val description: String = "Body armor"
    override val bonusStats: Stats = Stats(armor = 5, maxHealth = 50)
    override val type = ItemType.BODY_ARMOR
}
