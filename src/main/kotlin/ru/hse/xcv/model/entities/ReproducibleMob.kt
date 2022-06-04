package ru.hse.xcv.model.entities

import org.hexworks.zircon.api.data.Position

/*
 * A reproducible mob.
 */
abstract class ReproducibleMob(position: Position) : Mob(position) {
    abstract val reproduceCoolDown: Int

    /*
     * Reproduce with specified `coolDown` on `position`.
     */
    abstract fun clone(position: Position, coolDown: Int): ReproducibleMob
}
