package ru.hse.xcv.mapgen

import ru.hse.xcv.model.Field

interface FieldGenerationStrategy {
    fun generate(): Field
}
