package net.radstevee.packed.negativespaces

import net.radstevee.packed.core.hook.PackedHook
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import kotlin.math.abs

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
  private val min = abs(range.min())

  /** A mapping of character (index) to a width. */
  public val charToWidth: IntArray = IntArray(startUnicode + range.max()) { -1 }

  /** A mapping of width (index) to a character. */
  public val widthToChar: CharArray = CharArray(range.count()) { (-1).toChar() }

  override fun beforeSave(pack: ResourcePack) {
    range.forEachIndexed { idx, width ->
      charToWidth[idx] = width
      widthToChar[width + min] = idx.toChar()
    }

    pack.addFont {
      key = fontKey

      space {
        advances = charToWidth
          .withIndex()
          .filter { (_, width) -> width != -1 }
          .associate { (idx, width) -> idx.toChar().toString() to width.toDouble() }
          .toMap()
      }
    }
  }

  /**
   * Gets a space character for the given space width.
   * @param space The space width.
   * @return The character.
   */
  public fun getChar(space: Int): Char = widthToChar[space + min]

  /**
   * Gets the width of the given space character.
   * @param space The space character.
   * @return The width.
   */
  public fun getWidth(space: Char): Int = charToWidth[space.code]
}
