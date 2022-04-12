package ru.hse.xcv.view


import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.component.ComponentStyleSet
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.game.base.BaseGameArea
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed

import ru.hse.xcv.controllers.PlayerController
import ru.hse.xcv.util.InputManager

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

const val GAME_SCREEN_SPLIT_RATIO = 0.7

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

    val healthPanel  = Components.progressBar()
        .withNumberOfSteps(100)
        .withRange(100)
        .withDisplayPercentValueOfProgress(true)
        .withPreferredSize(infoPanelSize.width - 3, 3)
        .withDecorations(box(BoxType.BASIC))
        .withPosition(3, 1)
        .build()
    //healthPanel.progress += 10

    val xcvNamePanel  = Components.header()
        .withText("XCV")
        .withPreferredSize(3, 1)
        .withPosition(7, 0)
        .build()

    val hpNamePanel  = Components.header()
        .withText("HP:")
        .withPreferredSize(3, 2)
        .withPosition(0, 2)
        .build()
    infoPanel.addComponent(xcvNamePanel)
    infoPanel.addComponent(hpNamePanel)
    infoPanel.addComponent(healthPanel)

    val horizontalSplit = Components.hbox()
        .withPreferredSize(Size.create(width, height))
        .build()

    horizontalSplit.addComponent(gamePanel)
    horizontalSplit.addComponent(infoPanel)

    gameScreen.addComponent(horizontalSplit)

    val inputManager = InputManager(5)

    gameScreen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
        if (event.code in PlayerController.SUPPORTED_KEYS) {
            inputManager.offer(event.code)
        }
        Processed
    }

    return GameScreen(gameScreen, gameArea, inputManager)
}
