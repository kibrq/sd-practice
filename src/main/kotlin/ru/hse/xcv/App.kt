package ru.hse.xcv

import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.CP437TilesetResources


import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.mapgen.RandomPatternFieldGenerationStrategy
import ru.hse.xcv.view.createGameScreen
import ru.hse.xcv.util.Graphics

import kotlinx.coroutines.*

fun main() {
    val windowSize = Size.create(40, 20)
    val fieldSize = Size.create(200, 100)

    val (window, world, field, input) = createGameScreen(
        AppConfig.newBuilder()
            .withSize(windowSize)
            .withDefaultTileset(CP437TilesetResources.hack64x64())
            .withFpsLimit(90)
            .build(),
        RandomPatternFieldGenerationStrategy(fieldSize),
        Graphics.default()
    )

 

    window.display()
    
    window.onShutdown { world.dispose() }

    runBlocking {
           val hero = field.dynamicLayer.forEach { 
        if(it.value is Hero) {
            repeat(it.value.position.x) {world.scrollOneRight()}
            repeat(it.value.position.y) {world.scrollOneForward()}
        }
    }
        launch {
            while(true) {
                val a = input.take()
                when(a) {
                    KeyCode.KEY_W -> world.scrollOneBackward()
                    KeyCode.KEY_S -> world.scrollOneForward()
                    KeyCode.KEY_A -> world.scrollOneLeft()
                    KeyCode.KEY_D -> world.scrollOneRight()
                }
            }
        }
    }
}
