package ru.hse.xcv.model

import ru.hse.xcv.util.Coordinate

abstract class DynamicObject(
    var position: Coordinate,
    var direction: Coordinate,
    var moveSpeed: Int
) {
}
