package ru.hse.xcv.util

import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import ru.hse.xcv.view.FieldView
import kotlin.math.abs
import kotlin.math.roundToInt

val possibleDirections: List<Position> = buildList {
    for (i in -1..1) {
        for (j in -1..1) {
            if (i == 0 && j == 0) continue
            add(Position.create(i, j))
        }
    }
}

fun Logger.debug(msg: Any?) = debug(msg.toString())

fun Position.normalize() = Position.create(x / maxOf(abs(x), abs(y), 1), y / maxOf(abs(x), abs(y), 1))

fun <T> Map<Position, T>.readRect(rect: Rect) =
    rect.fetchPositions()
        .map { it to this[it] }
        .filter { it.second != null }
        .toMap()


fun FieldView.makeCentered(position: Position) {
    val (w, h) = this.visibleSize
    val (x, y) = position

    val newPosition = Position.create(maxOf(x - w / 2, 0), maxOf(y - h / 2, 0)).toPosition3D(0)
    scrollTo(newPosition)
}

// line through two points equation
private fun Position.yOnPathTo(x: Int, other: Position) =
    if (x != other.x) {
        (this.y + (x - this.x) * (other.y - this.y).toDouble() / (other.x - this.x)).roundToInt()
    } else other.y

private fun Position.xOnPathTo(y: Int, other: Position) =
    Position.create(this.y, this.x).yOnPathTo(y, Position.create(other.y, other.x))

fun Position.straightPathTo(other: Position): List<Position> {
    val xRange = if (x < other.x) x..other.x else other.x..x
    val yRange = if (y < other.y) y..other.y else other.y..y
    // line through two points equation
    val xList = xRange.map { it to yOnPathTo(it, other) }
    val yList = yRange.map { xOnPathTo(it, other) to it }
    return (xList + yList).distinct().map { Position.create(it.first, it.second) }
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = (first + other.first) to (second + other.second)

fun List<Pair<Int, Int>>.sum(): Pair<Int, Int> = fold(0 to 0) { acc, cur -> acc + cur }

fun <T> MutableCollection<T>.addAll(vararg elem: T) = addAll(elem.toList())

