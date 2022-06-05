package ru.hse.xcv.model.entities.mobs.dungeon

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.entities.mobs.AbstractMobFactory

class DungeonMobFactory : AbstractMobFactory() {
    override fun createDragon(position: Position) = DungeonDragon(position)
    override fun createMaxim(position: Position) = DungeonMaxim(position)
    override fun createMicrochel(position: Position) = DungeonMicrochel(position)
    override fun createPoisonousMold(position: Position) = DungeonMold(position)
    override fun createZombie(position: Position) = DungeonZombie(position)
}
