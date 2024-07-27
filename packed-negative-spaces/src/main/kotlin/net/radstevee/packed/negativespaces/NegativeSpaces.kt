package net.radstevee.packed.negativespaces

import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.plugin.PackedPlugin

/**
 * Represents a font with negative width spaces, to be used for shifting things.
 * @param fontKey The font key to use for this font.
 * @param range The negative space range. Default is -8192 to 8192.
 */
class NegativeSpaces(
    val fontKey: Key = Key("minecraft", "default"),
    val range: IntRange = -8192..8192,
) : PackedPlugin {
    /**
     * The space advances.
     */
    val advances =
        buildMap {
            range.forEachIndexed { i, it ->
                put((START_UNICODE + i).toChar(), it.toDouble())
            }
        }.toMutableMap()

    override fun beforeSave(pack: ResourcePack) {
        pack.addFont {
            key = fontKey

            space {
                advances = this@NegativeSpaces.advances
            }
        }
    }

    /**
     * Gets a space character for the given space width.
     * @param space The space width.
     * @return The character.
     */
    fun getChar(space: Int) = advances.filterValues { it == space.toDouble() }.keys.first()

    companion object {
        /**
         * The start unicode character to be used for this.
         */
        var START_UNICODE = 0xCE000
    }
}
