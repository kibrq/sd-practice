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
import ru.hse.xcv.model.FieldModel
import ru.hse.xcv.model.FieldTile
import ru.hse.xcv.util.InputManager
import ru.hse.xcv.view.WorldTile

typealias FieldView = GameArea<Tile, WorldTile>

class CustomGameArea(
    visibleSize: Size,
    actualSize: Size
) : BaseGameArea<Tile, WorldTile>(
    initialVisibleSize = visibleSize.toSize3D(10),
    initialActualSize = actualSize.toSize3D(100),
    initialFilters = listOf()
)

data class GameScreen(
    val window: Screen,
    val world: FieldView,
    val input: InputManager,
)

const val GAME_SCREEN_SPLIT_RATIO = 0.8

fun createGameScreen(config: AppConfig): GameScreen {
    val gameScreen = Screen.create(SwingApplications.startTileGrid(config))

    val (width, height) = config.size
    val gameAreaVisibleSize = Size.create((width * GAME_SCREEN_SPLIT_RATIO).toInt(), height)
    val gameAreaTotalSize = Size.create(Int.MAX_VALUE, Int.MAX_VALUE)

    val infoPanelSize = Size.create((width * (1 - GAME_SCREEN_SPLIT_RATIO)).toInt(), height)

    val gameArea = CustomGameArea(gameAreaVisibleSize, gameAreaTotalSize)

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
            while (!inputManager.offer(Pair(1, event.code)))
                inputManager.poll()
        }
        Processed
    }
    gameScreen.handleKeyboardEvents(KeyboardEventType.KEY_RELEASED) { event, _ ->
        if (PlayerController.SUPPORTED_KEYS.contains(event.code)) {
            while (!inputManager.offer(Pair(-1, event.code)))
                inputManager.poll()
        }
        Processed
    }

    return GameScreen(gameScreen, gameArea, inputManager)
}
