package ru.hse.xcv.model.spells

interface Spell {
    val name: String
    val description: String
    val coolDown: Int
}
