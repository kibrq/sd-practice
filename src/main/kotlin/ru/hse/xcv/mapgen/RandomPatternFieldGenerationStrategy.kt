package ru.hse.xcv.mapgen

import kotlin.collections.MutableList
import kotlin.random.Random

import org.hexworks.zircon.api.data.Position

import ru.hse.xcv.mapgen.FieldGenerationStrategy
import ru.hse.xcv.mapgen.Rectangle
import ru.hse.xcv.model.Field
import ru.hse.xcv.model.Tile
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.cos
import kotlin.sequences.generateSequence
import kotlin.math.floor


class RandomPatternFieldGenerationStrategy: FieldGenerationStrategy {
    override fun generate(bounds: Rectangle): Field {

    }

    private fun generateRandomPointsOnCircle(o: Position, r: Int, nPoints: Int): List<Position> {
        return generateSequence { Random.nextDouble(0.0, 2 * PI) }
            .take(nPoints)
            .sorted()
            .map { Position((o.x + sin(it) * r).toInt(), (o.y + cos(it) * r).toInt()) }
            .toList()
    }

    private fun generateRandomPolygon(bounds: Rectangle, nVertices: Int): List<Position> {
        
    }
}
