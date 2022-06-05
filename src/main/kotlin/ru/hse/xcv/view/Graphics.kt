package ru.hse.xcv.view

import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols
import ru.hse.xcv.model.FieldTile
import ru.hse.xcv.model.OnMapObject
import ru.hse.xcv.model.entities.Hero
import ru.hse.xcv.model.entities.PickableItem
import ru.hse.xcv.model.entities.mobs.cyberpunk.*
import ru.hse.xcv.model.entities.mobs.dungeon.*
import ru.hse.xcv.model.spells.FireballSpell
import kotlin.reflect.KClass

typealias WorldTile = Block<Tile>

/*
 * Creates a Block with given char and color.
 */
fun worldTileOfSymbolAndColor(char: Char, color: TileColor) =
    Block.newBuilder<Tile>()
        .withContent(
            Tile.newBuilder()
                .withCharacter(char)
                .withForegroundColor(color)
                .buildCharacterTile()
        )
        .withEmptyTile(Tile.empty())
        .build()

/*
 * A graphic representation of a static and dynamic layer.
 */
interface Graphics {
    fun staticLayerTransform(tile: FieldTile): WorldTile
    fun dynamicLayerTransform(obj: OnMapObject): WorldTile

    companion object {
        /*
         * Get default Graphics.
         */
        fun default() = FromMapGraphics(
            mapOf(
                FieldTile.FLOOR to worldTileOfSymbolAndColor(Symbols.INTERPUNCT, ANSITileColor.YELLOW),
                FieldTile.WALL to worldTileOfSymbolAndColor('#', TileColor.fromString("#999999"))
            ),
            mapOf(
                Hero::class to worldTileOfSymbolAndColor(Symbols.FACE_WHITE, ANSITileColor.GREEN),
                PickableItem::class to worldTileOfSymbolAndColor(
                    Symbols.INVERTED_QUESTION_MARK,
                    ANSITileColor.BRIGHT_WHITE
                ),
                FireballSpell.Fireball::class to worldTileOfSymbolAndColor(
                    Symbols.SOLAR_SYMBOL,
                    ANSITileColor.BRIGHT_RED
                ),
            ) + mapOf(
                DungeonDragon::class to worldTileOfSymbolAndColor(Symbols.SECTION_SIGN, ANSITileColor.BRIGHT_RED),
                DungeonMaxim::class to worldTileOfSymbolAndColor(Symbols.FEMALE, ANSITileColor.BRIGHT_YELLOW),
                DungeonZombie::class to worldTileOfSymbolAndColor(Symbols.YEN, ANSITileColor.BRIGHT_RED),
                DungeonMicrochel::class to worldTileOfSymbolAndColor(Symbols.CENT, ANSITileColor.CYAN),
                DungeonMold::class to worldTileOfSymbolAndColor(
                    Symbols.BLOCK_MIDDLE,
                    ANSITileColor.BRIGHT_RED
                )
            ) + mapOf(
                CyberpunkDragon::class to worldTileOfSymbolAndColor(Symbols.PILCROW, ANSITileColor.BRIGHT_RED),
                CyberpunkMaxim::class to worldTileOfSymbolAndColor(Symbols.MALE, ANSITileColor.BRIGHT_YELLOW),
                CyberpunkZombie::class to worldTileOfSymbolAndColor(Symbols.POUND, ANSITileColor.BRIGHT_RED),
                CyberpunkMicrochel::class to worldTileOfSymbolAndColor(Symbols.F_WITH_HOOK, ANSITileColor.CYAN),
                CyberpunkMold::class to worldTileOfSymbolAndColor(
                    Symbols.BLOCK_DENSE,
                    ANSITileColor.BRIGHT_RED
                )
            )
        )
    }
}

/*
 * Default Graphics.
 */
class FromMapGraphics(
    private val staticLayerTransformMap: Map<FieldTile, WorldTile>,
    private val dynamicLayerTransformMap: Map<KClass<out OnMapObject>, WorldTile>
) : Graphics {
    override fun staticLayerTransform(tile: FieldTile) = staticLayerTransformMap.getValue(tile)
    override fun dynamicLayerTransform(obj: OnMapObject) = dynamicLayerTransformMap.getValue(obj::class)
}
