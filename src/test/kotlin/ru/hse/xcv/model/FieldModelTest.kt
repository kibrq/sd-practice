package ru.hse.xcv.model

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Rect
import org.hexworks.zircon.api.data.Size
import org.junit.jupiter.api.Test
import ru.hse.xcv.model.entities.mobs.dungeon.DungeonMicrochel
import ru.hse.xcv.model.entities.mobs.dungeon.DungeonZombie
import kotlin.test.assertEquals

internal class FieldModelTest {
    @Test
    fun `Test field model`() {
        val pos00 = Position.create(0, 0)
        val pos01 = Position.create(0, 1)
        val pos10 = Position.create(1, 0)
        val pos11 = Position.create(1, 1)

        val staticLayer = mapOf(
            pos00 to FieldTile.FLOOR,
            pos01 to FieldTile.FLOOR,
            pos10 to FieldTile.WALL,
            pos11 to FieldTile.WALL
        )
        val dynamicLayer = mutableMapOf(
            pos00 to DungeonZombie(pos00) as OnMapObject,
            pos01 to DungeonMicrochel(pos01) as OnMapObject
        )
        val rect = Rect.create(Position.zero(), Size.create(100, 100))

        val fieldModel = FieldModel(staticLayer, dynamicLayer, rect)

        for (pos in listOf(pos00, pos01, pos10, pos11)) {
            assertEquals(fieldModel.byPosition(pos), staticLayer[pos] to dynamicLayer[pos])
        }
    }
}
