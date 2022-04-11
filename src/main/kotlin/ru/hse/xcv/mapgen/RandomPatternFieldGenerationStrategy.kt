package ru.hse.xcv.mapgen

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Size
import ru.hse.xcv.model.Field
import ru.hse.xcv.model.FieldTile
import ru.hse.xcv.util.readRect


class RandomPatternFieldGenerationStrategy(
    private val size: Size,
    private val smoothTimes: Int = 8,
) : FieldGenerationStrategy {

    override fun generate(): Field {
        var tiles = size.fetchPositions().associateWith {
            if (Math.random() < 0.5) FieldTile.FLOOR else FieldTile.WALL
        }

        repeat(smoothTimes) {
            tiles = size.fetchPositions().associateWith { pos ->
                val neighborhood = tiles.readRect(
                    Rect.create(pos - Position.offset1x1(), Size.create(3, 3))
                )
                val floors = neighborhood.filter { it.value == FieldTile.FLOOR }.count()
                val rocks = neighborhood.filter { it.value == FieldTile.WALL }.count()
                if (floors >= rocks) FieldTile.FLOOR else FieldTile.WALL
            }
        }
        return Field(tiles, mutableMapOf(), Rect.create(Position.zero(), size))
    }
}
