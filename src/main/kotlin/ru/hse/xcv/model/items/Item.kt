package ru.hse.xcv.model.items

import ru.hse.xcv.model.stats.Stats

/*
 * An item that can be equipped on a hero.
 */
interface Item {
    val name: String
    val description: String
    val bonusStats: Stats
    val type: ItemType

    companion object {
        private val allItems = listOf(
            { Sword() },
            { Helmet() },
            { BodyArmor() },
            { Shield() }
        )

        /*
         * Returns a random item.
         */
        fun getRandomItem() = allItems.random().invoke()
    }
}
