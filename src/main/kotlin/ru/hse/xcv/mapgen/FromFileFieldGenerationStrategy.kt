package ru.hse.xcv.mapgen

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.hse.xcv.model.FieldModel
import ru.hse.xcv.util.fieldFromJsonRepresentation
import ru.hse.xcv.util.fieldToJsonRepresentation
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class FromFileFieldGenerationStrategy(
    private val fileName: String
) : FieldGenerationStrategy {
    init {
        require(Files.isRegularFile(Path.of(fileName)))
    }

    override fun generate(): FieldModel {
        val sourceFile = File(fileName)
        val json = sourceFile.readText()
        return fieldFromJsonRepresentation(Json.decodeFromString(json))
    }

    fun saveField(field: FieldModel) {
        val sourceFile = File(fileName)
        val json = Json.encodeToString(fieldToJsonRepresentation(field))
        sourceFile.writeText(json)
    }
}
