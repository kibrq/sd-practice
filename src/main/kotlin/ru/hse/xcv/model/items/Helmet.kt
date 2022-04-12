package ru.hse.xcv.model.items

import ru.hse.xcv.model.stats.Stats

class Helmet : Item {
    override val name: String = "Helmet"
    override val description: String = "Helmet"
    override val bonusStats: Stats = Stats(armor = 3, maxHealth = 20)
}
