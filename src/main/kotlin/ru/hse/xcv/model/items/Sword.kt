package ru.hse.xcv.model.items

import ru.hse.xcv.model.stats.Stats

/*
 * An item that increases hero's power.
 */
class Sword : Item {
    override val name: String = "Sword"
    override val description: String = "Sword"
    override val bonusStats: Stats = Stats(power = 5)
    override val type = ItemType.SWORD
}
