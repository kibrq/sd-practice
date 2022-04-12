package ru.hse.xcv.model.items

import ru.hse.xcv.model.stats.Stats

class Sword : Item {
    override val name: String = "Sword"
    override val description: String = "Sword"
    override val bonusStats: Stats = Stats(power = 5)
}
