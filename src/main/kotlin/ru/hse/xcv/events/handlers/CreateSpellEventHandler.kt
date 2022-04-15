package ru.hse.xcv.events.handlers

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.events.CreateSpellEvent
import ru.hse.xcv.model.spells.ChainLightningSpell
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.model.spells.HealSpell
import ru.hse.xcv.world.World

class CreateSpellEventHandler(override val world: World) : EventHandler<CreateSpellEvent> {
    private fun useChainLightning(spell: ChainLightningSpell, position: Position) {

    }

    private fun useFireballSpell(spell: FireballSpell, position: Position) {
    }

    private fun useHealSpell(spell: HealSpell) {
        val hero = world.hero
        val amount = spell.healAmount(hero.level)
        hero.heal(amount)
    }

    override fun handle(event: CreateSpellEvent) {
        when (event.spell) {
            is ChainLightningSpell -> useChainLightning(event.spell, event.position)
            is FireballSpell -> useFireballSpell(event.spell, event.position)
            is HealSpell -> useHealSpell(event.spell)
        }
    }
}
