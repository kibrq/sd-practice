package ru.hse.xcv.events.handlers

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.events.CastSpellEvent
import ru.hse.xcv.events.DamageEvent
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.spells.ChainLightningSpell
import ru.hse.xcv.model.spells.FireballSpell
import ru.hse.xcv.model.spells.HealSpell
import ru.hse.xcv.util.possibleDirections
import ru.hse.xcv.world.World

class CastSpellEventHandler(
    override val world: World,
    private val eventBus: EventBus
) : EventHandler<CastSpellEvent> {
    private fun getDirectionsPrioritized(position: Position, direction: Position): List<Position> {
        val secondPriority = Position.zero() - direction
        val otherPositions = possibleDirections.filter { it != direction && it != secondPriority }
        return listOf(direction, secondPriority) + otherPositions
    }

    private fun useChainLightning(spell: ChainLightningSpell, level: Int, pos: Position, directions: List<Position>) {

    }

    private fun useFireballSpell(spell: FireballSpell, level: Int, pos: Position, directions: List<Position>) {
        for (direction in directions) {
            val fireball = spell.createFireball(level, pos + direction, direction)
            if (world.createObject(fireball, fireball.position)) {
                break
            }
        }
        for (direction in directions) {
            val fireball = spell.createFireball(level, pos + direction, direction)
            val entity = world.model.dynamicLayer[fireball.position] as? Entity ?: continue
            if (entity !is Hero) {
                val event = DamageEvent(entity, fireball.damage)
                eventBus.fire(event)
            }
            break
        }
    }

    private fun useHealSpell(spell: HealSpell, level: Int) {
        val amount = spell.healAmount(level)
        world.hero.heal(amount)
    }

    override fun handle(event: CastSpellEvent) {
        val directions = getDirectionsPrioritized(event.position, event.direction)
        when (event.spell) {
            is ChainLightningSpell -> useChainLightning(event.spell, event.level, event.position, directions)
            is FireballSpell -> useFireballSpell(event.spell, event.level, event.position, directions)
            is HealSpell -> useHealSpell(event.spell, event.level)
        }
    }
}
