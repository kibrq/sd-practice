package ru.hse.xcv

import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Size
import ru.hse.xcv.controllers.ActionControllerFactory
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.SpellBookChangeEvent
import ru.hse.xcv.events.SwitchScreenEvent
import ru.hse.xcv.input.GameInputManager
import ru.hse.xcv.mapgen.FieldGenerationStrategy
import ru.hse.xcv.mapgen.RandomPatternFieldGenerationStrategy
import ru.hse.xcv.util.makeCentered
import ru.hse.xcv.view.*
import ru.hse.xcv.world.World
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment

private val gd: GraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
private val screenWidth = gd.displayMode.width
private val screenHeight = gd.displayMode.height

private const val FPS_LIMIT = 60
private const val XCV = "xcv"
private val WINDOW_SIZE = Size.create(screenWidth / 40, screenHeight / 40) // don't ask why
private val TILESET = CP437TilesetResources.sirHenry32x32()
private val FIELD_SIZE = Size.create(100, 100)

/*
 * Start xcv.
 */
fun startGame(
    view: FieldView,
    panelControllers: PanelControllers,
    inventoryList: InventoryItemList,
    inputManager: GameInputManager,
    strategy: FieldGenerationStrategy,
    eventBus: EventBus
) {
    val world = World(
        strategy.generate(),
        view,
        Graphics.default(),
        ActionControllerFactory(eventBus, inputManager)
    )

    eventBus.registerGameHandlers(world, panelControllers)

    eventBus.registerInventoryEventHandlers(world, inventoryList)

    world.hero.let {
        view.makeCentered(it.position)
        eventBus.fire(SpellBookChangeEvent(it.spellBook))
    }

    world.start()
}

fun main() {
    val appConfig = AppConfig.newBuilder()
        .withTitle(XCV)
        .withSize(WINDOW_SIZE)
        .withDefaultTileset(TILESET)
        .withFpsLimit(FPS_LIMIT)
        .build()

    val eventBus = EventBus()

    val mainWindow = createMainScreen(appConfig, eventBus)
    val (gameState, gameView, panelControllers) = createGameScreen(appConfig, eventBus)
    val (inventoryState, inventoryList) = createInventoryScreen(appConfig, eventBus)

    eventBus.registerScreenEventHandlers(
        mainWindow,
        listOf(gameState, inventoryState)
    )

    startGame(
        gameView,
        panelControllers,
        inventoryList,
        gameState.input,
        RandomPatternFieldGenerationStrategy(FIELD_SIZE),
        eventBus
    )

    mainWindow.screen.onShutdown { gameView.dispose() }

    eventBus.fire(SwitchScreenEvent(State.Type.GAME))

    mainWindow.screen.display()
}
