package ru.hse.xcv

import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import ru.hse.xcv.controllers.ActionControllerFactory
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.mapgen.FieldGenerationStrategy
import ru.hse.xcv.mapgen.RandomPatternFieldGenerationStrategy
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.util.InputManager
import ru.hse.xcv.util.makeCentered
import ru.hse.xcv.view.GameScreen
import ru.hse.xcv.view.Graphics
import ru.hse.xcv.view.createGameScreen
import ru.hse.xcv.world.World
import java.awt.GraphicsDevice
import java.awt.GraphicsEnvironment


private val gd: GraphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
private val screenWidth = gd.displayMode.width
private val screenHeight = gd.displayMode.height

private const val FPS_LIMIT = 30
private const val XCV = "xcv"
private val WINDOW_SIZE = Size.create(screenWidth / 40, screenHeight / 40) // don't ask why
private val TILESET = CP437TilesetResources.sirHenry32x32()
private val FIELD_SIZE = Size.create(100, 100)

fun startGame(
    gameScreen: GameScreen,
    inputManager: InputManager,
    strategy: FieldGenerationStrategy,
    bus: EventBus
) {
    val (window, view, panelControllers) = gameScreen

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

    window.onShutdown { view.dispose() }
    window.display()
    world.start()
}

fun createInputManager(eventBus: EventBus, screen: Screen): InputManager {
    val inputManager = InputManager(eventBus)

    screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
        if (event.code in InputManager.SUPPORTED_KEYS) {
            inputManager.keyPressed(event.code)
        }
        Processed
    }

    screen.handleKeyboardEvents(KeyboardEventType.KEY_RELEASED) { event, _ ->
        if (event.code in InputManager.SUPPORTED_KEYS) {
            inputManager.keyReleased(event.code)
        }
        Processed
    }

    return inputManager
}


fun main() {
    val appConfig = AppConfig.newBuilder()
        .withTitle(XCV)
        .withSize(WINDOW_SIZE)
        .withDefaultTileset(TILESET)
        .withFpsLimit(FPS_LIMIT)
        .build()

    val eventBus = EventBus()
    val gameScreen = createGameScreen(appConfig)
    val inputManager = createInputManager(eventBus, gameScreen.window)

    startGame(
        gameScreen,
        inputManager,
        RandomPatternFieldGenerationStrategy(FIELD_SIZE),
        eventBus
    )
}
