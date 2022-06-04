package ru.hse.xcv.mapgen

import ru.hse.xcv.model.FieldModel

/*
 * An interface representing a strategy to generate FieldModel.
 */
interface FieldGenerationStrategy {
    /*
     * Generates an FieldModel object.
     */
    fun generate(): FieldModel
}
