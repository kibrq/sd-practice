package ru.hse.xcv

import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.SwingApplications
import ru.hse.xcv.controllers.ActionControllerFactory
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.mapgen.FieldGenerationStrategy
import ru.hse.xcv.mapgen.RandomPatternFieldGenerationStrategy
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.input.GameInputManager
import ru.hse.xcv.util.makeCentered
import ru.hse.xcv.view.FieldView
import ru.hse.xcv.view.PanelControllers
import ru.hse.xcv.view.Graphics
import ru.hse.xcv.view.createMainScreen
import ru.hse.xcv.view.createInventoryScreen
import ru.hse.xcv.view.createGameScreen
import ru.hse.xcv.world.World
import ru.hse.xcv.view.State
import ru.hse.xcv.events.SwitchScreenEvent
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

fun startGame(
    view: FieldView,
    panelControllers: PanelControllers,
    inputManager: GameInputManager,
    strategy: FieldGenerationStrategy,
    bus: EventBus
) {
    val world = World(
        strategy.generate(),
        view,
        Graphics.default(),
        ActionControllerFactory(bus, inputManager)
    )

    bus.registerGameHandlers(world, panelControllers)

    world.getAllObjectsOfType(Hero::class).keys.first().let {
        view.makeCentered(it.position)
        it.spellBook.allSpells().forEach { spell ->
            panelControllers.spellsPanelController.addSpell(spell)
        }
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

    val mainWindow                              = createMainScreen(appConfig, eventBus)
    val (gameState, gameView, panelControllers) = createGameScreen(appConfig, eventBus)
    val (inventoryState, inventoryList)         = createInventoryScreen(appConfig, eventBus)

    eventBus.registerScreenEventHandlers(
        mainWindow,
        listOf<State>(gameState, inventoryState)
    )

    eventBus.registerInventoryEventHandlers(
        inventoryList
    )

    startGame(
        gameView,
        panelControllers,
        gameState.input,
        RandomPatternFieldGenerationStrategy(FIELD_SIZE),
        eventBus
    )

    mainWindow.screen.onShutdown { gameView.dispose() }

    eventBus.fire(SwitchScreenEvent(State.Type.GAME))

    mainWindow.screen.display()
}
