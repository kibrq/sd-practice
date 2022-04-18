package ru.hse.xcv.model.spells

import org.hexworks.zircon.api.data.Size

class ChainLightningSpell : Spell {
    override val name = "Chain lightning"
    override val description = "Confuses nearest enemy"
    override var combination = "HJKL"
    override var coolDown = 5
    val durationMillis = 2000L
    val range = Size.create(10, 10)
}
