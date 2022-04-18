package ru.hse.xcv.controllers

import ru.hse.xcv.events.EventBus

class PickableItemController(
    override val eventBus: EventBus
) : ActionController {
    override fun action() = false
}
