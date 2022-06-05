package ru.hse.xcv.util

import kotlinx.serialization.Serializable
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Size
import ru.hse.xcv.model.FieldModel
import ru.hse.xcv.model.FieldTile
import ru.hse.xcv.model.OnMapObject
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.PickableItem
import ru.hse.xcv.model.entities.mobs.AbstractMobFactory
import kotlin.reflect.KType

@Serializable
data class JsonRepresentationPosition(val x: Int, val y: Int)

@Serializable
data class JsonRepresentationSize(val width: Int, val height: Int)

@Serializable
data class JsonRepresentationRect(
    val position: JsonRepresentationPosition,
    val size: JsonRepresentationSize
)

@Serializable
data class JsonRepresentationOnMapObject(
    val position: JsonRepresentationPosition,
    val type: String
)

@Serializable
data class JsonRepresentationField(
    val staticLayer: List<Pair<JsonRepresentationPosition, FieldTile>>,
    val dynamicLayer: List<Pair<JsonRepresentationPosition, JsonRepresentationOnMapObject>>,
    val rect: JsonRepresentationRect
)

private fun positionToJsonRepresentation(p: Position): JsonRepresentationPosition {
    return JsonRepresentationPosition(p.x, p.y)
}

private fun sizeToJsonRepresentation(s: Size): JsonRepresentationSize {
    return JsonRepresentationSize(s.width, s.height)
}

private fun rectToJsonRepresentation(r: Rect): JsonRepresentationRect {
    return JsonRepresentationRect(
        positionToJsonRepresentation(r.position),
        sizeToJsonRepresentation(r.size)
    )
}

private fun onMapObjectToJsonRepresentation(
    o: OnMapObject,
    mobTransform: Map<KType, String>
): JsonRepresentationOnMapObject {
    var repr: String? = null
    mobTransform.forEach { (clazz, representation) ->
        if (clazz.javaClass.isInstance(o)) {
            repr = representation
        }
    }
    return JsonRepresentationOnMapObject(
        positionToJsonRepresentation(o.position),
        repr ?: o.javaClass.typeName.substringAfterLast(".")
    )
}

fun fieldToJsonRepresentation(
    f: FieldModel,
    mobFactory: AbstractMobFactory
): JsonRepresentationField {
    val mobTransform = mapOf(
        mobFactory::createDragon.returnType to "Dragon",
        mobFactory::createMaxim.returnType to "Maxim",
        mobFactory::createMicrochel.returnType to "Microchel",
        mobFactory::createPoisonousMold.returnType to "PoisonousMold",
        mobFactory::createZombie.returnType to "Zombie"
    )
    return JsonRepresentationField(
        f.staticLayer.toList().map { (k, v) -> positionToJsonRepresentation(k) to v },
        f.dynamicLayer.toList().map { (k, v) ->
            positionToJsonRepresentation(k) to onMapObjectToJsonRepresentation(v, mobTransform)
        },
        rectToJsonRepresentation(f.rect)
    )
}

private fun positionFromJsonRepresentation(p: JsonRepresentationPosition): Position {
    return Position.create(p.x, p.y)
}

private fun sizeFromJsonRepresentation(s: JsonRepresentationSize): Size {
    return Size.create(s.width, s.height)
}

private fun rectFromJsonRepresentation(r: JsonRepresentationRect): Rect {
    return Rect.create(positionFromJsonRepresentation(r.position), sizeFromJsonRepresentation(r.size))
}

private fun onMapObjectFromJsonRepresentation(
    o: JsonRepresentationOnMapObject,
    mobFactory: AbstractMobFactory
): OnMapObject {
    val position = positionFromJsonRepresentation(o.position)
    return when (o.type) {
        "Hero" -> Hero(position)
        "Dragon" -> mobFactory.createDragon(position)
        "Maxim" -> mobFactory.createMaxim(position)
        "Microchel" -> mobFactory.createMicrochel(position)
        "PoisonousMold" -> mobFactory.createPoisonousMold(position)
        "Zombie" -> mobFactory.createZombie(position)
        "PickableItem" -> PickableItem.getRandomPickableItem(position)
        else -> throw IllegalStateException()
    }
}

fun fieldFromJsonRepresentation(
    f: JsonRepresentationField,
    mobFactory: AbstractMobFactory
): FieldModel {
    return FieldModel(
        f.staticLayer.associate { positionFromJsonRepresentation(it.first) to it.second },
        mutableMapOf(*f.dynamicLayer.map {
            positionFromJsonRepresentation(it.first) to onMapObjectFromJsonRepresentation(it.second, mobFactory)
        }.toTypedArray()),
        rectFromJsonRepresentation(f.rect)
    )
}
