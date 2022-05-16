package ru.hse.xcv.model.items

import ru.hse.xcv.model.stats.Stats
import kotlin.random.Random


interface Item {
    val name: String
    val description: String
    val bonusStats: Stats

    companion object {
        private val allItems = listOf(
            { Sword() },
            { Helmet() },
            { BodyArmor() },
            { Shield() }
        )

        fun getRandomItem() = allItems.random().invoke()
    }
}
