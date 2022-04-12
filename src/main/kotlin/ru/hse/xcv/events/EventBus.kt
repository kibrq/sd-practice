package ru.hse.xcv.events

class EventBus {
    private val eventQueue: ArrayDeque<Event> = ArrayDeque()
    val none = EventDispatcher<NoneEvent>()
    val move = EventDispatcher<MoveEvent>()
    val buff = EventDispatcher<BuffEvent>()
    val createSpell = EventDispatcher<CreateSpellEvent>()
    val damage = EventDispatcher<DamageEvent>()
    val letterPressed = EventDispatcher<LetterPressed>()

    fun fire(event: Event) {
        event.callback?.action()
        when (event) {
            is NoneEvent -> none.run(event)
            is MoveEvent -> move.run(event)
            is BuffEvent -> buff.run(event)
            is CreateSpellEvent -> createSpell.run(event)
            is DamageEvent -> damage.run(event)
            is LetterPressed -> letterPressed.run(event)
        }
    }
}
