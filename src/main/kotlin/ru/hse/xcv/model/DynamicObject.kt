package ru.hse.xcv.model

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size

abstract class DynamicObject {
    abstract var position: Position
    abstract var moveSpeed: Int
    open var direction: Position = Position.zero()
    open var fieldOfView: Size = Size.create(20, 20)
}
