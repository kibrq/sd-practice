package ru.hse.xcv.events

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.entities.Entity
import ru.hse.xcv.model.entities.Mob
import ru.hse.xcv.model.spells.Spell
import ru.hse.xcv.model.spells.book.SpellBook
import ru.hse.xcv.model.stats.Stats

/*
 * An interface representing any game event.
 */
sealed interface GameEvent : Event

/*
 * Move `obj` by `offset`. Specify if moving world(user's field of view) needed.
 * Crazy movements flag enables more moving options to bypass obstacles.
 */
data class MoveEvent(
    val obj: DynamicObject,
    val offset: Position,
    val moveWorld: Boolean = false,
    val crazyMovements: Boolean = false
) : GameEvent

/*
 * Buffs `entity`s stats with `buff`.
 */
data class BuffEvent(
    val entity: Entity,
    val buff: Stats
) : GameEvent

/*
 * Cast `spell` from `position` in `direction` with power level `power`.
 */
data class CastSpellEvent(
    val spell: Spell,
    val position: Position,
    val direction: Position,
    val power: Int
) : GameEvent

/*
 * Change HP of `entity` by amount.
 */
class HPChangeEvent private constructor(
    val entity: Entity,
    val amount: Int
) : GameEvent {
    companion object {
        /*
         * Damages `entity` by `damage`.
         */
        fun createDamageEvent(entity: Entity, damage: Int) = HPChangeEvent(entity, -damage)

        /*
         * Heals `entity` by `heal`.
         */
        fun createHealEvent(entity: Entity, heal: Int) = HPChangeEvent(entity, heal)
    }
}

/*
 * `letter` was pressed by user.
 */
data class LetterPressedEvent(
    val letter: Char
) : GameEvent

/*
 * View must show `spellBook`s spells.
 */
data class SpellBookChangeEvent(
    val spellBook: SpellBook
) : GameEvent

/*
 * Create `mob` with cool down `coolDown`.
 */
data class CreateMobEvent(
    val mob: Mob,
    val refer: Entity?,
    val coolDown: Int = 0
) : GameEvent
