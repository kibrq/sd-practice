package ru.hse.xcv.view

import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.input.DefaultInputManager
import ru.hse.xcv.input.InputManager

data class MainScreen(
    val screen: Screen,
    var input: InputManager
)

/*
 * Creates main screen.
 */
fun createMainScreen(config: AppConfig, bus: EventBus): MainScreen {
    val mainScreen = MainScreen(
        Screen.create(SwingApplications.startTileGrid(config)),
        DefaultInputManager(bus)
    )

    mainScreen.screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
        mainScreen.input.keyPressed(event.code)
        Processed
    }

    mainScreen.screen.handleKeyboardEvents(KeyboardEventType.KEY_RELEASED) { event, _ ->
        mainScreen.input.keyReleased(event.code)
        Processed
    }

    return mainScreen
}
