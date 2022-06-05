package ru.hse.xcv.mapgen

import ru.hse.xcv.model.FieldModel
import ru.hse.xcv.model.entities.mobs.AbstractMobFactory

/*
 * A builder for a FieldModel.
 */
abstract class FieldGenerationBuilder(private val mobFactory: AbstractMobFactory) {
    /*
     * Build a FieldModel.
     */
    abstract fun build(): FieldModel
}
