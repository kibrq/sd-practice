package ru.hse.xcv.view


import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.game.base.BaseGameArea
import org.hexworks.zircon.api.graphics.Symbols
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import ru.hse.xcv.controllers.PlayerController
import ru.hse.xcv.mapgen.FieldGenerationStrategy
import ru.hse.xcv.model.Field
import ru.hse.xcv.model.FieldTile
import ru.hse.xcv.util.InputManager
import ru.hse.xcv.util.Graphics
import ru.hse.xcv.util.WorldTile

typealias GameWorld = GameArea<Tile, WorldTile>
typealias BaseGameWorld = BaseGameArea<Tile, WorldTile>

class CustomGameArea(
    visibleSize: Size,
    actualSize: Size
) : BaseGameWorld(
    initialVisibleSize = visibleSize.toSize3D(10),
    initialActualSize = actualSize.toSize3D(100),
    initialFilters = listOf()
)

data class GameScreen(
    val window: Screen,
    val world: GameWorld,
    val field: Field,
    val input: InputManager,
)

const val GAME_SCREEN_SPLIT_RATIO = 0.8

fun createGameScreen(config: AppConfig, strategy: FieldGenerationStrategy, graphics: Graphics): GameScreen {
    val gameScreen = Screen.create(SwingApplications.startTileGrid(config))

    val (width, height) = config.size
    val gameAreaVisibleSize = Size.create((width * GAME_SCREEN_SPLIT_RATIO).toInt(), height)
    val gameAreaTotalSize = Size.create(Int.MAX_VALUE, Int.MAX_VALUE)

    val infoPanelSize = Size.create((width * (1 - GAME_SCREEN_SPLIT_RATIO)).toInt(), height)

    val field = strategy.generate()
    val gameArea = CustomGameArea(gameAreaVisibleSize, gameAreaTotalSize)

    putFieldOnGameWorld(gameArea, field, graphics)

    val gamePanel = Components.panel()
        .withPreferredSize(gameAreaVisibleSize)
        .withComponentRenderer(GameComponents.newGameAreaComponentRenderer(gameArea, ProjectionMode.TOP_DOWN))
        .build()

    val infoPanel = Components.panel()
        .withPreferredSize(infoPanelSize)
        .build()

    val horizontalSplit = Components.hbox()
        .withPreferredSize(Size.create(width, height))
        .build()

    horizontalSplit.addComponent(gamePanel)
    horizontalSplit.addComponent(infoPanel)

    gameScreen.addComponent(horizontalSplit)

    val inputManager = InputManager(5)

    gameScreen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
        if (PlayerController.SUPPORTED_KEYS.contains(event.code)) {
            while (!inputManager.offer(event.code))
                inputManager.poll()
        }
        Processed
    }

    return GameScreen(gameScreen, gameArea, field, inputManager)
}


fun putFieldOnGameWorld(world: GameWorld, field: Field, graphics: Graphics) {
    field.rect.fetchPositions().forEach { pos ->
        val fieldTile = field.staticLayer[pos]
        if (fieldTile != null)
            world.setBlockAt(pos.toPosition3D(0), graphics.staticLayerTransform(fieldTile))
    }
    field.rect.fetchPositions().forEach { pos ->
        val obj = field.dynamicLayer[pos]
        if (obj != null)
            world.setBlockAt(pos.toPosition3D(1), graphics.dynamicLayerTransform(obj))
    }
}
