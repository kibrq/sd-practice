package ru.hse.xcv.util

import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Position

fun <T> Map<Position, T>.readRect(rect: Rect) = 
    rect.fetchPositions()
        .map { it to this.get(it) }
        .filter { it.second != null }
        .toMap()
