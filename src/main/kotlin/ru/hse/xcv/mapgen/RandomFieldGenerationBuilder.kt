package ru.hse.xcv.mapgen

import org.hexworks.zircon.api.data.Size
import ru.hse.xcv.model.FieldModel

/*
 * A random builder for a FieldModel.
 */
class RandomFieldGenerationBuilder : FieldGenerationBuilder() {
    private var size: Size = Size.create(100, 100)
    private var smoothTimes = 5
    private var hardness = 1
    private var floorPercentage = 0.55

    fun setSize(size: Size): FieldGenerationBuilder {
        check(size.width >= 50)
        check(size.height >= 50)
        this.size = size
        return this
    }

    fun setWidth(width: Int): FieldGenerationBuilder {
        check(width >= 50)
        this.size = Size.create(width, size.height)
        return this
    }

    fun setHeight(height: Int): FieldGenerationBuilder {
        check(height >= 50)
        this.size = Size.create(size.width, height)
        return this
    }

    fun setSmoothTimes(smoothTimes: Int): FieldGenerationBuilder {
        check(smoothTimes > 0)
        this.smoothTimes = smoothTimes
        return this
    }

    fun setHardness(hardness: Int): FieldGenerationBuilder {
        check(hardness > 0)
        this.hardness = hardness
        return this
    }

    fun setFloorPercentage(floorPercentage: Double): FieldGenerationBuilder {
        check(floorPercentage in 0.0..1.0)
        this.floorPercentage = floorPercentage
        return this
    }

    /*
     * Randomly builds FieldModel.
     */
    override fun build(): FieldModel {
        return RandomPatternFieldGenerationStrategy(size, smoothTimes, hardness, floorPercentage).generate()
    }
}
