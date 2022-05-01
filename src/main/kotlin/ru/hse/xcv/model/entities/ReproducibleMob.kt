package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.world.World

abstract class ReproducibleMob(position: Position) : Mob(position) {
    abstract val reproducityCooldown: Int
    abstract fun clone(position: Position, cooldown: Int): ReproducibleMob
}
