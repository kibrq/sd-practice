package ru.hse.xcv.model.items

import ru.hse.xcv.model.stats.Stats

class Shield : Item {
    override val name: String = "Shield"
    override val description: String = "Shield"
    override val bonusStats: Stats = Stats(armor = 5)
    override val type = ItemType.SHIELD
}
