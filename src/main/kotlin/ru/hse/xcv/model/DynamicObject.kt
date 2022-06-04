package ru.hse.xcv.model

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size

/*
 * An object on a map that can move and has a direction and a field of view.
 */
abstract class DynamicObject : OnMapObject {
    abstract override var position: Position
    abstract var moveSpeed: Int
    open var direction: Position = Position.zero()
    open var fieldOfView: Size = Size.create(20, 20)
}
