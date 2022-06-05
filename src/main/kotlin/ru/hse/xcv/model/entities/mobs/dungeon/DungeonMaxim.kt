package ru.hse.xcv.model.entities.mobs.dungeon

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.entities.mobs.Maxim
import ru.hse.xcv.model.stats.Stats

/*
 * A mediocre aggressive mob.
 */
class DungeonMaxim(position: Position) : Maxim(position) {
    override var moveSpeed = 20
    override val experienceGain = 40
    override val stats = Stats(
        power = 2,
        armor = 5,
        maxHealth = 50
    )
}
