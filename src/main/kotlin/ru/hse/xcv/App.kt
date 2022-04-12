package ru.hse.xcv

import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Size
import ru.hse.xcv.controllers.ActionControllerFactory
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.mapgen.FieldGenerationStrategy
import ru.hse.xcv.mapgen.RandomPatternFieldGenerationStrategy
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.util.makeCentered
import ru.hse.xcv.view.GameScreen
import ru.hse.xcv.view.Graphics
import ru.hse.xcv.view.createGameScreen
import ru.hse.xcv.world.World

private const val FPS_LIMIT = 30
private val WINDOW_SIZE = Size.create(26, 14)
private val TILESET = CP437TilesetResources.hack64x64()
private val FIELD_SIZE = Size.create(100, 100)

fun startGame(
    gameScreen: GameScreen,
    strategy: FieldGenerationStrategy,
    bus: EventBus
) {
    val (window, view, input) = gameScreen

    val world = World(strategy.generate(), view, Graphics.default(), ActionControllerFactory(bus, input))
    bus.registerGameHandlers(world)

    world.getObjectsByType(Hero::class).keys.first().let {
        view.makeCentered(it.position)
    }

    window.onShutdown { view.dispose() }
    window.display()
    world.start()
}


fun main() {
    val appConfig = AppConfig.newBuilder()
        .withTitle("xcv")
        .withSize(WINDOW_SIZE)
        .withDefaultTileset(TILESET)
        .withFpsLimit(FPS_LIMIT)
        .build()

    val gameScreen = createGameScreen(appConfig)

    val eventBus = EventBus()

    startGame(
        gameScreen,
        RandomPatternFieldGenerationStrategy(FIELD_SIZE),
        eventBus
    )
}
