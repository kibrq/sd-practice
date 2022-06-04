package ru.hse.xcv.model

import kotlinx.serialization.Serializable
import org.hexworks.zircon.api.behavior.HasSize
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect

/*
 * Impassable WALL tile or passable FLOOR tile.
 */
@Serializable
enum class FieldTile {
    WALL, FLOOR
}

/*
 * Encapsulates a static and dynamic layer of the map.
 */
class FieldModel(
    val staticLayer: Map<Position, FieldTile>,
    val dynamicLayer: MutableMap<Position, OnMapObject>,
    val rect: Rect
) : HasSize {
    override val size = rect.size

    /*
     * Returns a pair of static layer and dynamic layer tile in specified `pos`.
     */
    fun byPosition(pos: Position) = staticLayer[pos] to dynamicLayer[pos]
}
