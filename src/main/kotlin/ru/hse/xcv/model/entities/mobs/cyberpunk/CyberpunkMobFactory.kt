package ru.hse.xcv.model.entities.mobs.cyberpunk

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.entities.mobs.AbstractMobFactory

class CyberpunkMobFactory : AbstractMobFactory() {
    override fun createDragon(position: Position) = CyberpunkDragon(position)
    override fun createMaxim(position: Position) = CyberpunkMaxim(position)
    override fun createMicrochel(position: Position) = CyberpunkMicrochel(position)
    override fun createPoisonousMold(position: Position) = CyberpunkMold(position)
    override fun createZombie(position: Position) = CyberpunkZombie(position)
}
