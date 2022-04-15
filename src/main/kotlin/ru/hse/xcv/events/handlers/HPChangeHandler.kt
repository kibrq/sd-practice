package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.HPChangeEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.view.HealthPanelController
import ru.hse.xcv.world.World

class HPChangeHandler(
    override val world: World,
    private val healthPanelController: HealthPanelController
) :
    EventHandler<HPChangeEvent> {
    override fun handle(event: HPChangeEvent) {
        val entity = event.entity
        entity.changeHP(event.amount)
        if (entity is Hero) {
            healthPanelController.setHealth(entity.stats.currentHealth, entity.stats.maxHealth)
        }
    }
}
