package ru.hse.xcv.mapgen

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Size
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.FieldModel
import ru.hse.xcv.model.FieldTile
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.util.readRect

fun recursiveSplit(rect: Rect, threshold: Size): List<Rect> {
    val (x, y, width, height) = rect

    if (width < threshold.width || height < threshold.height) {
        return listOf(rect)
    }

    val (first, second) = if (width < height) {
        rect.splitVertical(height / 2)
    } else {
        rect.splitHorizontal(width / 2)
    }
    return recursiveSplit(first, threshold) + recursiveSplit(second, threshold)
}


class RandomPatternFieldGenerationStrategy(
    private val size: Size,
    private val smoothTimes: Int = 5,
    private val hardness: Int = 1,
    private val floorPercentage: Double = 0.55
) : FieldGenerationStrategy {

    private fun generateSmoothedTiles(): Map<Position, FieldTile> {
        var tiles = size.fetchPositions().associateWith {
            if (Math.random() < floorPercentage) FieldTile.FLOOR else FieldTile.WALL
        }

        val size3x3 = Size.create(3, 3)
        repeat(smoothTimes) {
            tiles = size.fetchPositions().associateWith { pos ->
                val neighborhood = tiles.readRect(
                    Rect.create(pos - Position.offset1x1(), size3x3)
                )
                val floors = neighborhood.count { it.value == FieldTile.FLOOR }
                val rocks = neighborhood.count { it.value == FieldTile.WALL }
                if (floors >= rocks) FieldTile.FLOOR else FieldTile.WALL
            }
        }

        return tiles
    }

    private fun generateDynamicLayer(tiles: Map<Position, FieldTile>): MutableMap<Position, DynamicObject> {
        val dynamicLayer = mutableMapOf<Position, DynamicObject>()

        val threshold = Size.create(20, 20)
        val mapRect = Rect.create(Position.zero(), size)

        recursiveSplit(mapRect, threshold).forEach { rect ->
            val floors = tiles.readRect(rect).filter { it.value == FieldTile.FLOOR }
            val mobCount = (hardness * floors.count()) / threshold.height / threshold.width + 1
            floors.asSequence().shuffled().take(mobCount).forEach {
                dynamicLayer[it.key] = Mob.getRandomMob(it.key)
            }
        }

        return dynamicLayer
    }

    private fun createHero(tiles: Map<Position, FieldTile>, dynamicLayer: MutableMap<Position, DynamicObject>) {
        val noMobsNearby = { pos: Position ->
            val corner = pos - Position.create(10, 10)
            val size = Size.create(20, 20)
            val rect = Rect.create(corner, size)
            tiles.readRect(rect).none { dynamicLayer.containsKey(it.key) }
        }

        tiles.filter { it.value == FieldTile.FLOOR && !dynamicLayer.containsKey(it.key) }
            .keys
            .filter(noMobsNearby)
            .randomOrNull()?.let {
                dynamicLayer[it] = Hero(it)
            }
    }

    override fun generate(): FieldModel {
        val tiles = generateSmoothedTiles()
        val dynamicLayer = generateDynamicLayer(tiles)
        createHero(tiles, dynamicLayer)

        return FieldModel(tiles, dynamicLayer, Rect.create(Position.zero(), size))
    }
}
