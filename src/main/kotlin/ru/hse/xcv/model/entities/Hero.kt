package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position

import ru.hse.xcv.model.stats.Experience
import ru.hse.xcv.model.items.Item
import ru.hse.xcv.model.stats.Stats
import ru.hse.xcv.model.spells.SpellBook

class Hero(
    position: Position,
    direction: Position,
    val isThisPlayer: Boolean = true
) : Entity(position, direction, defaultMoveSpeed, defaultStats) {
    val spellBook: SpellBook = SpellBook()
    val inventory: List<Item> = ArrayList()
    val experience: Experience = Experience()

    companion object {
        const val defaultMoveSpeed = 2
        val defaultStats = Stats(
            power = 5,
            armor = 5,
            currentHealth = 100,
            maxHealth = 100
        )
    }
}
