package ru.hse.xcv.model

import kotlin.collections.Map
import kotlin.collections.MutableMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlinx.serialization.Serializable

import org.hexworks.zircon.api.data.Position

@Serializable
enum class Tile() {
    NONE, WALL, FLOOR
}

typealias ReadOnlyNeighborhood = Pair<Map<Position, Tile>, Map<Position, DynamicObject>>

class Field(
    val staticLayer:  Map<Position, Tile>,
    val dynamicLayer: MutableMap<Position, DynamicObject>,
    val lock: ReentrantReadWriteLock = ReentrantReadWriteLock()
) {

}
