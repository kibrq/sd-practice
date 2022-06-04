package ru.hse.xcv.mapgen

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Size
import ru.hse.xcv.model.FieldModel
import ru.hse.xcv.model.FieldTile
import ru.hse.xcv.model.OnMapObject
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.model.entities.PickableItem
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
    private val smoothTimes: Int,
    private val hardness: Int,
    private val floorPercentage: Double
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

    private fun generateDynamicLayer(tiles: Map<Position, FieldTile>): MutableMap<Position, OnMapObject> {
        val dynamicLayer = mutableMapOf<Position, OnMapObject>()

        val mobThreshold = Size.create(20, 20)
        val mapRect = Rect.create(Position.zero(), size)

        recursiveSplit(mapRect, mobThreshold).forEach { rect ->
            val floors = tiles.readRect(rect).filter { it.value == FieldTile.FLOOR }
            val mobCount = (hardness * floors.count()) / mobThreshold.height / mobThreshold.width + 1
            floors.keys.shuffled().take(mobCount).forEach {
                dynamicLayer[it] = Mob.getRandomMob(it)
            }
        }

        return dynamicLayer
    }

    private fun placeItems(tiles: Map<Position, FieldTile>, dynamicLayer: MutableMap<Position, OnMapObject>) {
        val itemThreshold = Size.create(20, 20)
        recursiveSplit(Rect.create(Position.zero(), size), itemThreshold).forEach { rect ->
            val floors = tiles.readRect(rect).filter { it.value == FieldTile.FLOOR }
            floors.keys.random().let {
                dynamicLayer[it] = PickableItem.getRandomPickableItem(it)
            }
        }
    }

    private fun placeHero(tiles: Map<Position, FieldTile>, dynamicLayer: MutableMap<Position, OnMapObject>) {
        for (noMobsSize in 16 downTo 0) {
            val position = tiles.filter { it.value == FieldTile.FLOOR && !dynamicLayer.containsKey(it.key) }
                .keys
                .filter { pos ->
                    // check no mobs nearby
                    val corner = pos - Position.create(noMobsSize / 2, noMobsSize / 2)
                    val size = Size.create(noMobsSize, noMobsSize)
                    val rect = Rect.create(corner, size)
                    tiles.readRect(rect).none {
                        dynamicLayer.containsKey(it.key)
                    }
                }.randomOrNull()

            if (position != null) {
                dynamicLayer[position] = Hero(position)
                break
            }
        }
    }

    override fun generate(): FieldModel {
        val tiles = generateSmoothedTiles()
        val dynamicLayer = generateDynamicLayer(tiles)
        placeItems(tiles, dynamicLayer)
        placeHero(tiles, dynamicLayer)
        return FieldModel(tiles, dynamicLayer, Rect.create(Position.zero(), size))
    }
}
