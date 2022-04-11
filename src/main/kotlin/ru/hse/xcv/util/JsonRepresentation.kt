package ru.hse.xcv.util

import kotlinx.serialization.Serializable
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Size
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.Field
import ru.hse.xcv.model.FieldTile
import ru.hse.xcv.model.entities.Dragon
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Maxim
import ru.hse.xcv.model.entities.Zombie


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
data class JsonRepresentationDynamicObject(
    val position: JsonRepresentationPosition,
    val direction: JsonRepresentationPosition,
    val type: String
)

@Serializable
data class JsonRepresentationField(
    val staticLayer: List<Pair<JsonRepresentationPosition, FieldTile>>,
    val dynamicLayer: List<Pair<JsonRepresentationPosition, JsonRepresentationDynamicObject>>,
    val rect: JsonRepresentationRect
)

fun positionToJsonRepresentation(p: Position): JsonRepresentationPosition {
    return JsonRepresentationPosition(p.x, p.y)
}

fun sizeToJsonRepresentation(s: Size): JsonRepresentationSize {
    return JsonRepresentationSize(s.width, s.height)
}

fun rectToJsonRepresentation(r: Rect): JsonRepresentationRect {
    return JsonRepresentationRect(
        positionToJsonRepresentation(r.position),
        sizeToJsonRepresentation(r.size)
    )
}

fun dynamicObjectToJsonRepresentation(o: DynamicObject): JsonRepresentationDynamicObject {
    return JsonRepresentationDynamicObject(
        positionToJsonRepresentation(o.position),
        positionToJsonRepresentation(o.direction),
        o.javaClass.typeName.substringAfterLast(".")
    )
}

fun fieldToJsonRepresentation(f: Field): JsonRepresentationField {
    return JsonRepresentationField(
        f.staticLayer.toList().map { (k, v) -> positionToJsonRepresentation(k) to v },
        f.dynamicLayer.toList().map { (k, v) ->
            positionToJsonRepresentation(k) to dynamicObjectToJsonRepresentation(v)
        },
        rectToJsonRepresentation(f.rect)
    )
}

fun positionFromJsonRepresentation(p: JsonRepresentationPosition): Position {
    return Position.create(p.x, p.y)
}

fun sizeFromJsonRepresentation(s: JsonRepresentationSize): Size {
    return Size.create(s.width, s.height)
}

fun rectFromJsonRepresentation(r: JsonRepresentationRect): Rect {
    return Rect.create(positionFromJsonRepresentation(r.position), sizeFromJsonRepresentation(r.size))
}

fun dynamicObjectFromJsonRepresentation(o: JsonRepresentationDynamicObject): DynamicObject {
    return when (o.type) {
        "Hero" -> Hero(
            positionFromJsonRepresentation(o.position),
            positionFromJsonRepresentation(o.direction),
        )
        "Zombie" -> Zombie(
            positionFromJsonRepresentation(o.position),
            positionFromJsonRepresentation(o.direction),
        )
        "Maxim" -> Maxim(
            positionFromJsonRepresentation(o.position),
            positionFromJsonRepresentation(o.direction),
        )
        "Dragon" -> Dragon(
            positionFromJsonRepresentation(o.position),
            positionFromJsonRepresentation(o.direction),
        )
        else -> throw IllegalStateException()
    }
}

fun fieldFromJsonRepresentation(f: JsonRepresentationField): Field {
    return Field(
        f.staticLayer.associate { positionFromJsonRepresentation(it.first) to it.second },
        mutableMapOf(*f.dynamicLayer.map {
            positionFromJsonRepresentation(it.first) to dynamicObjectFromJsonRepresentation(
                it.second
            )
        }.toTypedArray()),
        rectFromJsonRepresentation(f.rect)
    )
}
