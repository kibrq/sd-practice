package ru.hse.xcv.view

import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Block
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols
import ru.hse.xcv.model.DynamicObject
import ru.hse.xcv.model.FieldTile
import ru.hse.xcv.model.entities.*
import ru.hse.xcv.model.spells.FireballSpell
import kotlin.reflect.KClass

typealias WorldTile = Block<Tile>

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


interface Graphics {
    fun staticLayerTransform(tile: FieldTile): WorldTile
    fun dynamicLayerTransform(obj: DynamicObject): WorldTile

    companion object {
        fun default() = FromMapGraphics(
            mapOf(
                FieldTile.FLOOR to worldTileOfSymbolAndColor(Symbols.INTERPUNCT, ANSITileColor.YELLOW),
                FieldTile.WALL to worldTileOfSymbolAndColor('#', TileColor.fromString("#999999"))
            ),
            mapOf(
                Hero::class to worldTileOfSymbolAndColor(Symbols.FACE_WHITE, ANSITileColor.GREEN),
                Dragon::class to worldTileOfSymbolAndColor(Symbols.SECTION_SIGN, ANSITileColor.RED),
                Maxim::class to worldTileOfSymbolAndColor(Symbols.FEMALE, ANSITileColor.YELLOW),
                Zombie::class to worldTileOfSymbolAndColor(Symbols.YEN, ANSITileColor.RED),
                Microchel::class to worldTileOfSymbolAndColor(Symbols.CENT, ANSITileColor.BRIGHT_CYAN),
                FireballSpell.Fireball::class to worldTileOfSymbolAndColor(
                    Symbols.SOLAR_SYMBOL,
                    ANSITileColor.BRIGHT_RED
                )
            )
        )
    }
}

class FromMapGraphics(
    private val staticLayerTransformMap: Map<FieldTile, WorldTile>,
    private val dynamicLayerTransformMap: Map<KClass<out DynamicObject>, WorldTile>,
) : Graphics {
    override fun staticLayerTransform(tile: FieldTile) = staticLayerTransformMap.getValue(tile)

    override fun dynamicLayerTransform(obj: DynamicObject) = dynamicLayerTransformMap.getValue(obj::class)
}
