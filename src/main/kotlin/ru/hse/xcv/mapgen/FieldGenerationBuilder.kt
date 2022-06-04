package ru.hse.xcv.mapgen

import ru.hse.xcv.model.FieldModel

/*
 * A builder for a FieldModel.
 */
abstract class FieldGenerationBuilder {
    /*
     * Build a FieldModel.
     */
    abstract fun build(): FieldModel
}
