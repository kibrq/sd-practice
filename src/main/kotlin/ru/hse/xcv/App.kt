package ru.hse.xcv

import org.hexworks.zircon.api.application.AppConfig

import org.hexworks.zircon.api.data.Size

import ru.hse.xcv.mapgen.RandomPatternFieldGenerationStrategy
import ru.hse.xcv.view.createGameScreen

fun main() {
    
    val windowSize = Size.create(200, 100)
    val fieldSize = Size.create(500, 500)

    val (window, world, field, input) = createGameScreen(
        AppConfig.newBuilder()
            .withSize(windowSize)
            .build(),
        RandomPatternFieldGenerationStrategy(fieldSize)
    )

    window.display()
}
