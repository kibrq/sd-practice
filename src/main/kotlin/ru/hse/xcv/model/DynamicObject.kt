package ru.hse.xcv.model

import org.hexworks.zircon.api.data.Position

abstract class DynamicObject(
    var position: Position,
    var direction: Position,
    var moveSpeed: Int
) {}
