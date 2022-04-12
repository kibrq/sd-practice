package ru.hse.xcv.controllers

import ru.hse.xcv.events.EventBus
import ru.hse.xcv.model.entities.Mob

class MobStrategy()

class MobController(
    private val mob: Mob,
    private val strategy: MobStrategy,
    override val eventFactory: EventBus,
) : ActionController {
    override fun action() {}
}
