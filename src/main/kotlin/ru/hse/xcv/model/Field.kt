package ru.hse.xcv.model

import kotlin.collections.Map
import kotlin.collections.MutableMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.behavior.HasSize
import org.hexworks.zircon.api.data.Rect

enum class FieldTile() {
    WALL, FLOOR
}

class Field(
    val staticLayer:  Map<Position, FieldTile>,
    val dynamicLayer: MutableMap<Position, DynamicObject>,
    val rect: Rect,
) : HasSize {
    private val lock = ReentrantReadWriteLock()
    override val size = rect.size
}
