package ru.hse.xcv.model.entities.mobs.dungeon

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.entities.mobs.PoisonousMold
import ru.hse.xcv.model.stats.Stats

/*
 * A mediocre reproducible mob.
 */
class DungeonMold(
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

    override fun clone(position: Position, coolDown: Int) = DungeonMold(position, coolDown)
}
