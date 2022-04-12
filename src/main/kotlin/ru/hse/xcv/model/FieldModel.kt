package ru.hse.xcv.model

import kotlinx.serialization.Serializable
import kotlin.collections.Map
import kotlin.collections.MutableMap
import java.util.concurrent.locks.ReentrantReadWriteLock

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.behavior.HasSize
import org.hexworks.zircon.api.data.Rect

@Serializable
enum class FieldTile {
    WALL, FLOOR
}

class FieldModel(
    val staticLayer:  Map<Position, FieldTile>,
    val dynamicLayer: MutableMap<Position, DynamicObject>,
    val rect: Rect,
) : HasSize {
    override val size = rect.size

    fun byPosition(pos: Position) = Pair(staticLayer.get(pos), dynamicLayer.get(pos))
}
