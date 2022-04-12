package ru.hse.xcv.model.spells

interface Spell {
    val name: String
    val combination: String
    val description: String
    val coolDown: Int
}
