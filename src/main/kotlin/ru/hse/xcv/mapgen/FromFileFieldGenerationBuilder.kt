package ru.hse.xcv.mapgen

import ru.hse.xcv.model.FieldModel

/*
 * A builder for a FieldModel from file.
 */
class FromFileFieldGenerationBuilder(private val fileName: String) : FieldGenerationBuilder() {
    /*
     * Builds FieldModel from `fileName`.
     */
    override fun build(): FieldModel {
        return FromFileFieldGenerationStrategy(fileName = fileName).generate()
    }
}
