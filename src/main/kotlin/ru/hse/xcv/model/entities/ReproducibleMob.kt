package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position

abstract class ReproducibleMob(position: Position) : Mob(position) {
    abstract val reproduceCoolDown: Int
    abstract fun clone(position: Position, coolDown: Int): ReproducibleMob
}
