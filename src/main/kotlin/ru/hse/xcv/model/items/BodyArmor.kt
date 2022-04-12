package ru.hse.xcv.model.items

import ru.hse.xcv.model.stats.Stats

class BodyArmor : Item {
    override val name: String = "Body armor"
    override val description: String = "Body armor"
    override val bonusStats: Stats = Stats(armor = 5, maxHealth = 50)
}
