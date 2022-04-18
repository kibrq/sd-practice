package ru.hse.xcv.events.handlers

import ru.hse.xcv.events.WTFModeEvent
import ru.hse.xcv.view.SpellsPanelController
import ru.hse.xcv.world.World

class WTFModeEventHandler(
    override val world: World,
    private val spellsPanelController: SpellsPanelController
) : EventHandler<WTFModeEvent> {
    override fun handle(event: WTFModeEvent) {
        spellsPanelController.clearSpells()
        event.spellBook.allSpells().forEach { spell ->
            spellsPanelController.addSpell(spell)
        }
    }
}
