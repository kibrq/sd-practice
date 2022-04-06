package ru.hse.xcv.model

import kotlin.collections.HashMap
import kotlin.collections.Map
import kotlin.collections.MutableMap
import java.util.concurrent.locks.ReentrantReadWriteLock

import ru.hse.xcv.util.Coordinate
import ru.hse.xcv.model.DynamicObject

enum class Tile() {}

class Field(
    val staticLayer:  Map<Coordinate, Tile>,
    val dynamicLayer: MutableMap<Coordinate, DynamicObject>,
    val lock:         ReentrantReadWriteLock
) {
    constructor(staticLayer: Map<Coordinate, Tile>, dynamicLayer: MutableMap<Coordinate, DynamicObject>): this(
        staticLayer = staticLayer,
        dynamicLayer = dynamicLayer,
        lock = ReentrantReadWriteLock()
    )
}
