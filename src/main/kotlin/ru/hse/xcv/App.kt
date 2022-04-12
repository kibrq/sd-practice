package ru.hse.xcv


import org.hexworks.zircon.api.CP437TilesetResources
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Size
import org.hexworks.zircon.api.uievent.KeyCode
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.MoveEvent
import ru.hse.xcv.events.handlers.MoveEventHandler
import ru.hse.xcv.mapgen.RandomPatternFieldGenerationStrategy
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.util.Graphics
import ru.hse.xcv.view.createGameScreen

fun main() {
    val windowSize = Size.create(100, 70)
    val fieldSize = Size.create(200, 100)

    val (window, world, field, input) = createGameScreen(
        AppConfig.newBuilder()
            .withSize(windowSize)
            .withDefaultTileset(CP437TilesetResources.cheepicus14x14())
            .withFpsLimit(90)
            .build(),
        RandomPatternFieldGenerationStrategy(fieldSize),
        Graphics.default()
    )


    val bus = EventBus()
    bus.move.register(MoveEventHandler(field, world))

    window.display()

    window.onShutdown { world.dispose() }

    val hero = field.dynamicLayer.map {
        if (it.value is Hero) {
            repeat(it.value.position.x) { world.scrollOneRight() }
            repeat(it.value.position.y) { world.scrollOneForward() }
            it.value
        } else null
    }.filterNotNull().first()

    while (true) {
        val a = input.take()
        when (a) {
            KeyCode.KEY_W -> Position.create(0, -1)
            KeyCode.KEY_S -> Position.create(0, 1)
            KeyCode.KEY_A -> Position.create(-1, 0)
            KeyCode.KEY_D -> Position.create(1, 0)
            else -> null
        }?.let {
            bus.fire(MoveEvent(hero, it, true, null))
        }
    }
}
