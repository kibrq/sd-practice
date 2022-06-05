package ru.hse.xcv.mapgen

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.hse.xcv.model.FieldModel
import ru.hse.xcv.model.entities.mobs.AbstractMobFactory
import ru.hse.xcv.util.fieldFromJsonRepresentation
import ru.hse.xcv.util.fieldToJsonRepresentation
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

/*
 * FieldGenerationStrategy to generate a FieldModel from a file.
 */
class FromFileFieldGenerationStrategy(
    private val fileName: String,
    private val mobFactory: AbstractMobFactory
) : FieldGenerationStrategy {
    init {
        require(Files.isRegularFile(Path.of(fileName)))
    }

    /*
     * Generates FieldModel from `fileName`.
     */
    override fun generate(): FieldModel {
        val sourceFile = File(fileName)
        val json = sourceFile.readText()
        return fieldFromJsonRepresentation(Json.decodeFromString(json), mobFactory)
    }

    /*
     * Saves specified `field` into `fileName`.
     */
    fun saveField(field: FieldModel) {
        val sourceFile = File(fileName)
        val json = Json.encodeToString(fieldToJsonRepresentation(field, mobFactory))
        sourceFile.writeText(json)
    }
}
