package ru.hse.xcv.mapgen

import ru.hse.xcv.model.FieldModel

interface FieldGenerationStrategy {
    fun generate(): FieldModel
}
