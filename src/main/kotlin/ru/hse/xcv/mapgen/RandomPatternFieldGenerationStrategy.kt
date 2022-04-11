package ru.hse.xcv.mapgen

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Rect

import ru.hse.xcv.mapgen.FieldGenerationStrategy
import ru.hse.xcv.model.Field
import ru.hse.xcv.model.FieldTile
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.util.readRect


class RandomPatternFieldGenerationStrategy(
    val size: Size,
    val smoothTimes: Int = 8,
): FieldGenerationStrategy {

    override fun generate(): Field {
        val (width, height) = size
        var tiles: MutableMap<Position, FieldTile> = mutableMapOf()
        size.fetchPositions().forEach { pos ->
            tiles[pos] = if (Math.random() < 0.5) FieldTile.FLOOR else FieldTile.WALL
        }
        val newTiles: MutableMap<Position, FieldTile> = mutableMapOf()

        repeat(smoothTimes) {
            size.fetchPositions().forEach { pos -> 
                val neighborhood = tiles.readRect(
                    Rect.create(pos - Position.offset1x1(), Size.create(3, 3))
                )
                val floors = neighborhood.filter { (_, value) -> value == FieldTile.FLOOR }.count()
                val rocks  = neighborhood.filter { (_, value) -> value == FieldTile.WALL }.count()
                newTiles.put(pos, if (floors >= rocks) FieldTile.FLOOR else FieldTile.WALL)
            }
            tiles = newTiles
        }
        return Field(tiles, mutableMapOf<Position, DynamicObject>(), Rect.create(Position.zero(), size))
    }
}
