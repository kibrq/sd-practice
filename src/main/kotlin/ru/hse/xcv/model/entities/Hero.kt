package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position

import ru.hse.xcv.model.stats.Experience
import ru.hse.xcv.model.items.Item
import ru.hse.xcv.model.stats.Stats
import ru.hse.xcv.model.spells.SpellBook

class Hero(
    position: Position,
    direction: Position,
    moveSpeed: Int,
    stats: Stats
) : Entity(position, direction, moveSpeed, stats) {
    val spellBook: SpellBook = SpellBook()
    val inventory: List<Item> = ArrayList()
    val experience: Experience = Experience()
}
