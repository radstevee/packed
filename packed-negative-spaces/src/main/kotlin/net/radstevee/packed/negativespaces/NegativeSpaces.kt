package net.radstevee.packed.negativespaces

import net.radstevee.packed.core.hook.PackedHook
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack

/**
 * Represents a font with negative width spaces, to be used for shifting things.
 * @param fontKey The font key to use for this font.
 * @param range The negative space range. Default is -8192 to 8192.
 * @param startUnicode The Unicode character that gets added to the space idx
 */
public class NegativeSpaces(
    public val fontKey: Key = Key("minecraft", "default"),
    public val range: IntRange = -8192 .. 8192,
    public val startUnicode: Int = 0xCE000
) : PackedHook {
    /**
     * The space advances.
     */
    public val advances: MutableMap<Char, Double> = buildMap {
        range.forEachIndexed { idx, width ->
            put((startUnicode + idx).toChar(), width.toDouble())
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
    public fun getChar(space: Int): Char = advances.filterValues { adv -> adv == space.toDouble() }.keys.first()
}
