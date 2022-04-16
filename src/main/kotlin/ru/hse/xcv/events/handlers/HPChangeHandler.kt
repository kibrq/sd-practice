package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.HPChangeEvent
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.view.HealthPanelController
import ru.hse.xcv.view.LevelPanelController
import ru.hse.xcv.world.World

class HPChangeHandler(
    override val world: World,
    private val healthPanelController: HealthPanelController,
    private val levelPanelController: LevelPanelController
) :
    EventHandler<HPChangeEvent> {
    override fun handle(event: HPChangeEvent) {
        val entity = event.entity
        entity.changeHP(event.amount)
        if (entity.isDead) {
            world.deleteObject(entity)
            if (entity is Mob) {
                world.hero.addExperience(entity.experienceGain)
                levelPanelController.setLevel(world.hero.experience.level)
            }
        }
        if (entity is Hero) {
            healthPanelController.setHealth(entity.stats.currentHealth, entity.stats.maxHealth)
        }
    }
}
