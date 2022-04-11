package ru.hse.xcv.model

import kotlin.collections.HashMap
import kotlin.collections.Map
import kotlin.collections.MutableMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlinx.serialization.Serializable

import ru.hse.xcv.util.Coordinate
import ru.hse.xcv.model.DynamicObject

@Serializable
enum class Tile() {
    NONE, WALL, FLOOR
}

typealias ReadOnlyNeighborhood = Pair<Map<Coordinate, Tile>, Map<Coordinate, DynamicObject>>

class Field(
    val staticLayer:  Map<Coordinate, Tile>,
    val dynamicLayer: MutableMap<Coordinate, DynamicObject>,
    val lock: ReentrantReadWriteLock = ReentrantReadWriteLock()
) {
    fun readNeighborhood(coordinate: Coordinate, width: Int, height: Int? = null): ReadOnlyNeighborhood {
        val widthRange = coordinate.x - width .. coordinate.x + width
        val heightRange = coordinate.y - (height ?: width) .. coordinate.y + (height ?: width)
        val desiredCoordiantes = widthRange.flatMap { wIt -> heightRange.map{ hIt-> Coordinate(wIt, hIt) } }
        return this.lock.read {
            ReadOnlyNeighborhood(
                desiredCoordiantes.map { it to this.staticLayer.get(it)!! }.toMap(),
                desiredCoordiantes.map { it to this.dynamicLayer.get(it)!! }.toMap()
            )
        }
    }
}
