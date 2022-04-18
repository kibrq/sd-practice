package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.OnMapObject
import ru.hse.xcv.model.items.Item

class PickableItem(override var position: Position, val item: Item) : OnMapObject {
    companion object {
        fun getRandomPickableItem(position: Position) = PickableItem(position, Item.getRandomItem())
    }
}
