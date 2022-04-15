package ru.hse.xcv.events.handlers

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.events.CreateSpellEvent
import ru.hse.xcv.model.spells.ChainLightningSpell
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.model.spells.HealSpell
import ru.hse.xcv.world.World

class CreateSpellEventHandler(override val world: World) : EventHandler<CreateSpellEvent> {
    private fun useChainLightning(spell: ChainLightningSpell, level: Int, position: Position) {

    }

    private fun useFireballSpell(spell: FireballSpell, level: Int, position: Position) {
        val fireball = spell.createFireball(position, level)
        world.createObject(fireball, position)
    }

    private fun useHealSpell(spell: HealSpell, level: Int) {
        val amount = spell.healAmount(level)
        world.hero.heal(amount)
    }

    override fun handle(event: CreateSpellEvent) {
        when (event.spell) {
            is ChainLightningSpell -> useChainLightning(event.spell, event.level, event.position)
            is FireballSpell -> useFireballSpell(event.spell, event.level, event.position)
            is HealSpell -> useHealSpell(event.spell, event.level)
        }
    }
}
