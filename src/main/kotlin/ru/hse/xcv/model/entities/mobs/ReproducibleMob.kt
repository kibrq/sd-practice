package ru.hse.xcv.model.entities.mobs

import org.hexworks.zircon.api.data.Position
import ru.hse.xcv.model.entities.Mob

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
