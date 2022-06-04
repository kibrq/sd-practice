package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.OnMapObject
import ru.hse.xcv.model.items.Item

/*
 * An item lying on the floor.
 */
class PickableItem(override var position: Position, val item: Item) : OnMapObject {
    companion object {
        /*
         * Returns a random pickable item.
         */
        fun getRandomPickableItem(position: Position) = PickableItem(position, Item.getRandomItem())
    }
}
