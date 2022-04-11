package ru.hse.xcv.events

class EventBus {
    private val eventQueue: ArrayDeque<Event> = ArrayDeque()
    val none = EventDispatcher<NoneEvent>()
    val move = EventDispatcher<MoveEvent>()
    val buff = EventDispatcher<BuffEvent>()
    val createSpell = EventDispatcher<CreateSpellEvent>()
    val damage = EventDispatcher<DamageEvent>()
    val letterPressed = EventDispatcher<LetterPressed>()

    private fun fire(event: Event) = when (event) {
        is NoneEvent -> none.run(event)
        is MoveEvent -> move.run(event)
        is BuffEvent -> buff.run(event)
        is CreateSpellEvent -> createSpell.run(event)
        is DamageEvent -> damage.run(event)
        is LetterPressed -> letterPressed.run(event)
    }

    fun addEvent(event: Event) {
        eventQueue.add(event)
    }

    fun fireEvents(amount: Int = eventQueue.size) {
        repeat(amount) {
            val event = eventQueue.removeFirstOrNull() ?: return
            fire(event)
        }
    }
}
