package ru.hse.xcv

import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Size

import ru.hse.xcv.events.EventBus
import ru.hse.xcv.mapgen.FieldGenerationStrategy
import ru.hse.xcv.mapgen.RandomPatternFieldGenerationStrategy
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.view.createGameScreen
import ru.hse.xcv.view.GameScreen
import ru.hse.xcv.view.Graphics
import ru.hse.xcv.world.World
import ru.hse.xcv.controllers.ActionControllerFactory
import ru.hse.xcv.util.makeCentered

val WINDOW_SIZE = Size.create(40, 20)

fun startGame(
    gameScreen: GameScreen,
    strategy: FieldGenerationStrategy,
    bus: EventBus,
) {
    val (window, view, input) = gameScreen
    
    val world = World(strategy.generate(), view, Graphics.default(), ActionControllerFactory(bus, input)) 
    bus.registerGameHandlers(world)

    world.getObjectsByType(Hero::class).first()?.let {
        view.makeCentered(it.position)
    }

    window.onShutdown { view.dispose() }
    window.display()
    world.start()
}


fun main() {
    val appConfig = AppConfig.newBuilder()
            .withSize(WINDOW_SIZE)
            .withDefaultTileset(CP437TilesetResources.hack64x64())
            .withFpsLimit(90)
            .build()

    val gameScreen = createGameScreen(appConfig)

    val eventBus = EventBus()

    startGame(
        gameScreen,
        RandomPatternFieldGenerationStrategy(Size.create(100, 100)),
        eventBus
    )
}
