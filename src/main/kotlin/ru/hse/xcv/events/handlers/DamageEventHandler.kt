package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.DamageEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.view.HealthPanelController
import ru.hse.xcv.view.PanelControllers
import ru.hse.xcv.world.World

class DamageEventHandler(
    override val world: World,
    private val healthPanelController: HealthPanelController
) :
    EventHandler<DamageEvent> {
    override fun handle(event: DamageEvent) {
        val entity = event.entity
        entity.receiveDamage(event.amount)
        if (entity is Hero) {
            healthPanelController.setHealth(entity.stats.currentHealth, entity.stats.maxHealth)
        }
    }
}
