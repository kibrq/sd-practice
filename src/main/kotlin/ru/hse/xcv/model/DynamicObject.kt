package ru.hse.xcv.model

import org.hexworks.zircon.api.data.Position

abstract class DynamicObject {
    abstract var position: Position
    abstract var moveSpeed: Int
    open var direction: Position = Position.zero()
}
