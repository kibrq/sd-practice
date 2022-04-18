package ru.hse.xcv.model.items

import ru.hse.xcv.model.stats.Stats
import kotlin.random.Random

interface Item {
    val name: String
    val description: String
    val bonusStats: Stats

    companion object {
        fun getRandomItem(): Item {
            return when (Random.nextInt(0, 4)) {
                0 -> Sword()
                1 -> Helmet()
                2 -> BodyArmor()
                3 -> Shield()
                else -> throw IllegalStateException()
            }
        }
    }
}
