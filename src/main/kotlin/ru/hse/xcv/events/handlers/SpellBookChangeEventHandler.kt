package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.SpellBookChangeEvent
import ru.hse.xcv.view.SpellsPanelController
import ru.hse.xcv.world.World

class SpellBookChangeEventHandler(
    override val world: World,
    private val spellsPanelController: SpellsPanelController
) : GameEventHandler<SpellBookChangeEvent> {
    override fun handle(event: SpellBookChangeEvent) {
        spellsPanelController.clearSpells()
        event.spellBook.allSpells().forEach {
            spellsPanelController.addSpell(it)
        }
    }
}
