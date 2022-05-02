package ru.hse.xcv.mapgen

import ru.hse.xcv.model.FieldModel

class FromFileFieldGenerationBuilder(private val fileName: String) : FieldGenerationBuilder() {
    override fun build(): FieldModel {
        return FromFileFieldGenerationStrategy(fileName = fileName).generate()
    }
}
