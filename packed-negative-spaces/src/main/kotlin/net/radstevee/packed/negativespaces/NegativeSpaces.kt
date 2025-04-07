package net.radstevee.packed.negativespaces

import net.radstevee.packed.core.hook.PackedHook
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack

/**
 * A hook to add a font with negative-width spaces, to be used for shifting things.
 */
public class NegativeSpaces(
  /** The font key to use. */
  public val fontKey: Key = Key("minecraft", "spaces"),
  /** The negative space range. */
  public val range: IntRange = -8192..8192,
  /** The start of the Unicode area. */
  public val startUnicode: Int = 0xCE000,
) : PackedHook {
  /** The space advances. */
  public val advances: Map<Char, Double> =
    buildMap {
      range.forEachIndexed { idx, width ->
        put((startUnicode + idx).toChar(), width.toDouble())
      }
    }

  override fun beforeSave(pack: ResourcePack) {
    pack.addFont {
      key = fontKey

      space {
        advances = this@NegativeSpaces.advances.mapKeys { (key) -> key.toString() }
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
