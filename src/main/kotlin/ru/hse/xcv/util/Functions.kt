package ru.hse.xcv.util

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect

fun <T> Map<Position, T>.readRect(rect: Rect) =
    rect.fetchPositions()
        .map { it to this[it] }
        .filter { it.second != null }
        .toMap()
