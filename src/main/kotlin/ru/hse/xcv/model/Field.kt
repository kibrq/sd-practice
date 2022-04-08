package ru.hse.xcv.model

import kotlin.collections.Map
import kotlin.collections.MutableMap
import java.util.concurrent.locks.ReentrantReadWriteLock

import ru.hse.xcv.util.Coordinate

enum class Tile {
    FLOOR,
    WALL,
    EMPTY
}

class Field(
    var staticLayer: Map<Coordinate, Tile>,
    var dynamicLayer: MutableMap<Coordinate, DynamicObject>,
    val lock: ReentrantReadWriteLock = ReentrantReadWriteLock()
) {
}
