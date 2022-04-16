package ru.hse.xcv.events.handlers

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.events.CastSpellEvent
import ru.hse.xcv.events.EventBus
import ru.hse.xcv.events.HPChangeEvent
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.spells.*
import ru.hse.xcv.util.possibleDirections
import ru.hse.xcv.world.World
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

class CastSpellEventHandler(
    override val world: World,
    private val eventBus: EventBus
) : EventHandler<CastSpellEvent> {
    private val coolDowns = mutableMapOf<KClass<out Spell>, Long>()
    private var wtfCoolDowns: Map<Spell, Int>? = null

    private fun getDirectionsPrioritized(position: Position, direction: Position): List<Position> {
        val secondPriority = Position.zero() - direction
        val otherPositions = possibleDirections.filter { it != direction && it != secondPriority }
        return listOf(direction, secondPriority) + otherPositions
    }

    private fun useWtfSpell() =
        wtfCoolDowns?.let { map ->
            // wtf turned off
            world.hero.moveSpeed /= 2
            world.hero.spellBook.allSpells().forEach { spell ->
                map[spell]?.let {
                    spell.coolDown = it
                }
            }
            wtfCoolDowns = null
        } ?: run {
            // wtf turned on
            world.hero.moveSpeed *= 2
            val spells = world.hero.spellBook.allSpells()
            wtfCoolDowns = spells.associateWith { it.coolDown }
            spells.forEach { it.coolDown = 0 }
        }

    private fun useChainLightning(spell: ChainLightningSpell, power: Int, pos: Position, directions: List<Position>) {

    }

    private fun useFireballSpell(spell: FireballSpell, power: Int, pos: Position, directions: List<Position>) {
        for (direction in directions) {
            val fireball = spell.createFireball(power, pos + direction, direction)
            if (world.createObject(fireball, fireball.position)) {
                break
            }
        }
        for (direction in directions) {
            val fireball = spell.createFireball(power, pos + direction, direction)
            val entity = world.getDynamicLayer(fireball.position) as? Entity ?: continue
            if (entity !is Hero) {
                val event = HPChangeEvent.createDamageEvent(entity, fireball.damage)
                eventBus.fire(event)
            }
            break
        }
    }

    private fun useHealSpell(spell: HealSpell, power: Int) {
        val amount = spell.healAmount(power)
        val event = HPChangeEvent.createHealEvent(world.hero, amount)
        eventBus.fire(event)
    }

    private fun useSeedBoostSpell(spell: SpeedBoostSpell) {
        val hero = world.hero
        val oldSpeed = hero.moveSpeed
        hero.moveSpeed = spell.newSpeed(oldSpeed)
        world.delayed(spell.durationMillis) {
            hero.moveSpeed = oldSpeed
        }
    }

    override fun handle(event: CastSpellEvent) {
        val currentTime = System.currentTimeMillis()
        coolDowns[event.spell::class]?.let {
            if (currentTime - it < TimeUnit.SECONDS.toMillis(event.spell.coolDown.toLong())) {
                return
            }
        }
        val directions = getDirectionsPrioritized(event.position, event.direction)
        when (event.spell) {
            is WtfSpell -> useWtfSpell()
            is ChainLightningSpell -> useChainLightning(event.spell, event.power, event.position, directions)
            is FireballSpell -> useFireballSpell(event.spell, event.power, event.position, directions)
            is HealSpell -> useHealSpell(event.spell, event.power)
            is SpeedBoostSpell -> useSeedBoostSpell(event.spell)
        }
        coolDowns[event.spell::class] = currentTime
    }
}
