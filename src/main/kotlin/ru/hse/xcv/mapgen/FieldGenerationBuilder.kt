package ru.hse.xcv.mapgen

import ru.hse.xcv.model.FieldModel

abstract class FieldGenerationBuilder {

    abstract fun build(): FieldModel
}
