package ru.hse.xcv.mapgen

import ru.hse.xcv.model.FieldModel
import ru.hse.xcv.model.entities.mobs.AbstractMobFactory

/*
 * A builder for a FieldModel from file.
 */
class FromFileFieldGenerationBuilder(
    private val fileName: String,
    private val mobFactory: AbstractMobFactory
) : FieldGenerationBuilder(mobFactory) {
    /*
     * Builds FieldModel from `fileName`.
     */
    override fun build(): FieldModel {
        return FromFileFieldGenerationStrategy(fileName, mobFactory).generate()
    }
}
