package ru.hse.xcv.model

import org.hexworks.zercon.api.data.Position

abstract class DynamicObject() {
    abstract var position: Position
    abstract var direction: Position
    abstract var moveSpeed: Int?
}
