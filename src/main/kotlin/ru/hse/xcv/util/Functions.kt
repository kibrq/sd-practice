package ru.hse.xcv.util

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect

import ru.hse.xcv.view.FieldView
import kotlin.math.abs

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
