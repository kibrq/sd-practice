package ru.hse.xcv.view


import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.component.ProgressBar
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.game.base.BaseGameArea
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen

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
    val view: FieldView,
    val panelControllers: PanelControllers
)

data class PanelControllers(
    val healthPanelController: HealthPanelController
)

class HealthPanelController(
    private val panel: ProgressBar
) {
    fun setHealth(hp: Int, maxHP: Int) {
        panel.progress = 100 * (hp.toDouble() / maxHP)
    }
}

const val GAME_SCREEN_SPLIT_RATIO = 0.7

fun createGameScreen(config: AppConfig): GameScreen {
    val screen = Screen.create(SwingApplications.startTileGrid(config))

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

    val xcvNamePanel = Components.header()
        .withText("XCV")
        .withPreferredSize(3, 1)
        .withPosition(infoPanelSize.width / 2, 0)
        .build()

    val hpNamePanel = Components.header()
        .withText("HP:")
        .withPreferredSize(3, 2)
        .withPosition(0, 2)
        .build()

    val healthPanel = Components.progressBar()
        .withNumberOfSteps(100)
        .withRange(100)
        .withDisplayPercentValueOfProgress(true)
        .withPreferredSize(infoPanelSize.width - hpNamePanel.width, 3)
        .withDecorations(box(BoxType.BASIC))
        .withPosition(Position.create(0, -1).relativeToRightOf(hpNamePanel))
        .build()
    healthPanel.progress = 100.0

    infoPanel.addComponent(xcvNamePanel)
    infoPanel.addComponent(hpNamePanel)
    infoPanel.addComponent(healthPanel)

    val horizontalSplit = Components.hbox()
        .withPreferredSize(Size.create(width, height))
        .build()

    horizontalSplit.addComponent(gamePanel)
    horizontalSplit.addComponent(infoPanel)

    screen.addComponent(horizontalSplit)

    return GameScreen(
        screen,
        gameArea,
        PanelControllers(
            HealthPanelController(healthPanel)
        )
    )
}
