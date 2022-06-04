package ru.hse.xcv.model.spells

/*
 * A hero's spell.
 */
sealed interface Spell {
    val name: String
    val description: String
    var combination: String
    var coolDown: Int
}
