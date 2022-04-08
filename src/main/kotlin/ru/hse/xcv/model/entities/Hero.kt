package ru.hse.xcv.model.entities

import ru.hse.xcv.model.stats.Experience
import ru.hse.xcv.model.items.Item
import ru.hse.xcv.model.stats.Stats
import ru.hse.xcv.model.spells.SpellBook
import ru.hse.xcv.util.Coordinate

class Hero(
    position: Coordinate,
    direction: Coordinate,
    moveSpeed: Int,
    stats: Stats
) : Entity(position, direction, moveSpeed, stats) {
    val spellBook: SpellBook = SpellBook()
    val inventory: List<Item> = ArrayList()
    val experience: Experience = Experience()
}
