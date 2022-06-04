package ru.hse.xcv.view

import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.GameComponents
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.Label
import org.hexworks.zircon.api.component.Panel
import org.hexworks.zircon.api.component.ProgressBar
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.game.base.BaseGameArea
import org.hexworks.zircon.api.graphics.BoxType
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.input.GameInputManager
import ru.hse.xcv.model.spells.Spell
import kotlin.math.min

/*
 * Main state of the game.
 */
class GameState(
    override val component: Component,
    override val input: GameInputManager
) : State {
    override val type = State.Type.GAME
}

typealias FieldView = GameArea<Tile, WorldTile>

/*
 * Custom game area.
 */
class CustomGameArea(
    visibleSize: Size,
    actualSize: Size
) : BaseGameArea<Tile, WorldTile>(
    initialVisibleSize = visibleSize.toSize3D(10),
    initialActualSize = actualSize.toSize3D(100),
    initialFilters = listOf()
)

data class GameScreen(
    val state: GameState,
    val view: FieldView,
    val panelControllers: PanelControllers
)

data class PanelControllers(
    val healthPanelController: HealthPanelController,
    val levelPanelController: LevelPanelController,
    val spellsPanelController: SpellsPanelController
)

/*
 * Controls GUI's health panel.
 */

class HealthPanelController(
    private val panel: ProgressBar
) {
    /*
     * Set health bar on GUI panel to `hp`.
     */
    fun setHealth(hp: Int, maxHP: Int) {
        panel.progress = 100 * (hp.toDouble() / maxHP)
    }
}

/*
 * Controls GUI's level panel.
 */
class LevelPanelController(
    private val panel: Label
) {
    /*
     * Set level to `level` in GUI.
     */
    fun setLevel(level: Int) {
        panel.text = level.toString()
    }
}

/*
 * Controls GUI's spell panel.
 */
class SpellsPanelController(
    private val panel: Panel
) {
    private var spellsNumber = 0

    /*
     * Clear all spells in GUI.
     */
    fun clearSpells() {
        panel.detachAllComponents()
        spellsNumber = 0
    }

    /*
     * Add a spell in GUI.
     */
    fun addSpell(spell: Spell) {
        val spellPanel = Components.panel()
            .withPreferredSize(panel.width, 2)
            .withPosition(0, 3 * spellsNumber)
            .build()

        val spellsNamePanel = Components.header()
            .withText(spell.name)
            .withPreferredSize(min(spellPanel.width, spell.name.length), 1)
            .withPosition(0, 0)
            .build()

        val spellsCombinationPanel = Components.panel()
            .withPreferredSize(spellPanel.width, 1)
            .withPosition(0, spellsNamePanel.height)
            .build()

        for ((i, c) in spell.combination.withIndex()) {
            val keyNamePanel = Components.label()
                .withText(c.uppercaseChar().toString())
                .withPreferredSize(2, 1)
                .withPosition(2 * i + (spellsCombinationPanel.width / 2 - spell.combination.length), 0)
                .build()
            spellsCombinationPanel.addComponent(keyNamePanel)
        }

        spellPanel.addComponent(spellsNamePanel)
        spellPanel.addComponent(spellsCombinationPanel)
        panel.addComponent(spellPanel)
        spellsNumber++
    }
}

const val GAME_SCREEN_SPLIT_RATIO = 0.7

/*
 * Creates main game screen.
 */
fun createGameScreen(config: AppConfig, eventBus: EventBus): GameScreen {
    val (width, height) = config.size
    val gameAreaVisibleSize = Size.create((width * GAME_SCREEN_SPLIT_RATIO).toInt(), height)
    val gameAreaTotalSize = Size.create(Int.MAX_VALUE, Int.MAX_VALUE)

    val infoPanelSize = Size.create((width * (1 - GAME_SCREEN_SPLIT_RATIO)).toInt(), height)

    val gameView = CustomGameArea(gameAreaVisibleSize, gameAreaTotalSize)

    val gamePanel = Components.panel()
        .withPreferredSize(gameAreaVisibleSize)
        .withComponentRenderer(GameComponents.newGameAreaComponentRenderer(gameView, ProjectionMode.TOP_DOWN))
        .build()

    val infoPanel = Components.panel()
        .withPreferredSize(infoPanelSize)
        .build()

    val xcvNamePanel = Components.header()
        .withText("XCV")
        .withPreferredSize(3, 1)
        .withPosition(infoPanelSize.width / 2 - 1, 0)
        .build()

    val levelPanel = Components.panel()
        .withPreferredSize(infoPanelSize.width, 1)
        .withPosition(0, 2)
        .build()

    val levelNamePanel = Components.header()
        .withText("Level:")
        .withPreferredSize(6, 1)
        .withPosition(0, 0)
        .build()

    val levelValuePanel = Components.label()
        .withText("1")
        .withPreferredSize(infoPanelSize.width - 6, 1)
        .withPosition(6, 0)
        .build()

    levelPanel.addComponent(levelNamePanel)
    levelPanel.addComponent(levelValuePanel)

    val hpNamePanel = Components.header()
        .withText("HP:")
        .withPreferredSize(3, 2)
        .withPosition(0, 4)
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

    val spellsNamePanel = Components.header()
        .withText("Spells:")
        .withPreferredSize(7, 1)
        .withPosition(infoPanelSize.width / 2 - 3, 7)
        .build()

    val spellsPanel = Components.panel()
        .withPreferredSize(infoPanelSize.width, infoPanelSize.height - 8)
        .withPosition(0, 8)
        .build()

    infoPanel.addComponent(xcvNamePanel)
    infoPanel.addComponent(levelPanel)
    infoPanel.addComponent(hpNamePanel)
    infoPanel.addComponent(healthPanel)
    infoPanel.addComponent(spellsNamePanel)
    infoPanel.addComponent(spellsPanel)

    val horizontalSplit = Components.hbox()
        .withPreferredSize(Size.create(width, height))
        .build()

    horizontalSplit.addComponent(gamePanel)
    horizontalSplit.addComponent(infoPanel)

    val gameState = GameState(
        component = horizontalSplit,
        input = GameInputManager(eventBus)
    )

    return GameScreen(
        gameState,
        gameView,
        PanelControllers(
            HealthPanelController(healthPanel),
            LevelPanelController(levelValuePanel),
            SpellsPanelController(spellsPanel)
        )
    )
}
