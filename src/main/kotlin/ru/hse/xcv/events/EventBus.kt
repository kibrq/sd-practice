package ru.hse.xcv.events

import ru.hse.xcv.world.World

import ru.hse.xcv.events.handlers.EventHandler
import ru.hse.xcv.events.handlers.MoveEventHandler
import ru.hse.xcv.events.handlers.DamageEventHandler
import ru.hse.xcv.events.handlers.CreateSpellEventHandler
import ru.hse.xcv.events.handlers.BuffEventHandler
import ru.hse.xcv.events.handlers.LetterPressedEventHandler

import kotlinx.coroutines.*

class EventBus {
    private val none = EventDispatcher<NoneEvent>()
    private val move = EventDispatcher<MoveEvent>()
    private val buff = EventDispatcher<BuffEvent>()
    private val createSpell = EventDispatcher<CreateSpellEvent>()
    private val damage = EventDispatcher<DamageEvent>()
    private val letterPressed = EventDispatcher<LetterPressedEvent>()

    fun fire(event: Event) {
        runBlocking {
            launch {
                delay(50)
                event.callback?.action()
            }
            when (event) {
                is NoneEvent -> none.run(event)
                is MoveEvent -> move.run(event)
                is BuffEvent -> buff.run(event)
                is CreateSpellEvent -> createSpell.run(event)
                is DamageEvent -> damage.run(event)
                is LetterPressedEvent -> letterPressed.run(event)
            }
        }
    }

    fun registerGameHandlers(world: World) {
        none.register(object : EventHandler<NoneEvent> {
            override val world = world

            override fun handle(event: NoneEvent) {}
        })

        move.register(MoveEventHandler(world))
        buff.register(BuffEventHandler(world))
        createSpell.register(CreateSpellEventHandler(world))
        damage.register(DamageEventHandler(world))
        letterPressed.register(LetterPressedEventHandler(world))
    }
}
