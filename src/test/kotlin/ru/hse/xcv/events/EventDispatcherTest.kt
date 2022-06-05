package ru.hse.xcv.events

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import ru.hse.xcv.events.handlers.EventHandler
import kotlin.test.assertContentEquals

internal class EventDispatcherTest {
    @Test
    fun `Test event dispatcher`() {
        val calledHandlers = mutableListOf<Int>()
        val dispatcher = EventDispatcher<Event>()
        val handler1 = mock<EventHandler<Event>> {
            on(it.handle(any())).doAnswer { calledHandlers.add(1).let {} }
        }
        val handler2 = mock<EventHandler<Event>> {
            on(it.handle(any())).doAnswer { calledHandlers.add(2).let {} }
        }
        val handler3 = mock<EventHandler<Event>> {
            on(it.handle(any())).doAnswer { calledHandlers.add(3).let {} }
        }
        dispatcher.register(handler1)
        dispatcher.register(handler2)
        dispatcher.register(handler3)

        dispatcher.run(mock())

        assertContentEquals(listOf(1, 2, 3), calledHandlers)
    }
}
