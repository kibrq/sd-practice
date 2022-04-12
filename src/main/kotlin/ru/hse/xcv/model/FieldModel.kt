package ru.hse.xcv.model

import kotlinx.serialization.Serializable
import org.hexworks.zircon.api.behavior.HasSize
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect

@Serializable
enum class FieldTile {
    WALL, FLOOR
}

class FieldModel(
    val staticLayer: Map<Position, FieldTile>,
    val dynamicLayer: MutableMap<Position, DynamicObject>,
    val rect: Rect
) : HasSize {
    override val size = rect.size

    fun byPosition(pos: Position) = Pair(staticLayer[pos], dynamicLayer[pos])
}
