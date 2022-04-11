package ru.hse.xcv.mapgen

import ru.hse.xcv.model.Field
import ru.hse.xcv.util.Coordinate

typealias Rectangle = Pair<Coordinate, Coordinate>

interface FieldGenerationStrategy {
    fun generate(bounds: Rectangle): Field
}
