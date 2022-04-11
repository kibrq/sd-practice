package ru.hse.xcv.mapgen

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.hse.xcv.model.Field
import ru.hse.xcv.util.fieldFromJsonRepresentation
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class FromFileFieldGenerationStrategy(
    private val fileName: String
) : FieldGenerationStrategy {
    init {
        require(Files.isRegularFile(Path.of(fileName)))
    }

    override fun generate(): Field {
        val sourceFile = File(fileName)
        val json = sourceFile.readText()
        return fieldFromJsonRepresentation(Json.decodeFromString(json))
    }
}
