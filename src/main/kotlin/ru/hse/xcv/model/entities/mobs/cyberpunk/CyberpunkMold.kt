package ru.hse.xcv.model.entities.mobs.cyberpunk

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.entities.mobs.PoisonousMold
import ru.hse.xcv.model.stats.Stats

/*
 * A mediocre reproducible mob.
 */
class CyberpunkMold(
    override var position: Position,
    override val reproduceCoolDown: Int = 15
) : PoisonousMold(position) {
    override var experienceGain = 10
    override var moveSpeed = 10
    override var stats = Stats(
        power = 5,
        armor = 5,
        maxHealth = 10
    )

    override fun clone(position: Position, coolDown: Int) = CyberpunkMold(position, coolDown)
}
