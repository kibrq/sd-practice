package ru.hse.xcv.controllers

import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.events.*
import ru.hse.xcv.input.*
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.PickableItem
import ru.hse.xcv.model.spells.book.WtfSpellBook
import ru.hse.xcv.util.sum
import ru.hse.xcv.world.World
import kotlin.math.abs

class PlayerController(
    private val world: World,
    private val input: GameInputManager,
    override val eventBus: EventBus
) : ActionController {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val hero: Hero
        get() = world.hero
    private var wtfSpellBook = WtfSpellBook()
    private var wtfMode = false

    private fun handleWtfMode() = input.zxc.let {
        if (it && !wtfMode) {
            hero.moveSpeed *= 2
            eventBus.fire(SpellBookChangeEvent(wtfSpellBook))
        } else if (!it && wtfMode) {
            hero.moveSpeed /= 2
            eventBus.fire(SpellBookChangeEvent(hero.spellBook))
        }
        wtfMode = it
    }

    private fun handleMovement() {
        val currentPressedKeys = input.currentMovementKeys
        val (x, y) = currentPressedKeys.map {
            when (it) {
                UP -> 0 to -1
                DOWN -> 0 to 1
                LEFT -> -1 to 0
                RIGHT -> 1 to 0
                else -> 0 to 0
            }
        }.sum()

        if (abs(x) + abs(y) > 0) {
            hero.direction = Position.create(x, y)
            val newPosition = hero.position + hero.direction
            val obj = world.getDynamicLayer(newPosition)
            val event = if (obj is PickableItem) {
                hero.inventory.add(obj.item)
                world.deleteObject(obj)
                UpdateInventoryEvent(hero.inventory, hero.equippedItems)
            } else {
                MoveEvent(hero, hero.direction, moveWorld = true)
            }
            eventBus.fire(event)
        }
    }

    private fun handleSpellCast() {
        val combination = input.readySpell ?: return
        val spellBook = if (wtfMode) wtfSpellBook else hero.spellBook
        val spell = spellBook.search(combination) ?: return
        logger.debug("${spell.name} was casted!")
        val event = CastSpellEvent(spell, hero.position, hero.direction, hero.power)
        eventBus.fire(event)
    }

    override fun action(): Boolean {
        handleWtfMode()
        handleMovement()
        handleSpellCast()
        return true
    }
}
