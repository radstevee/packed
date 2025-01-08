package net.radstevee.packed.core.font

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.radstevee.packed.core.key.Key

/**
 * Represents a provider within a font file.
 *
 * [See the Minecraft wiki for more information.](https://minecraft.wiki/w/Font#Providers)
 */
@Serializable
public sealed class FontProvider {
    /**
     * A bitmap font provider. Allows you to add a coloured image to a font.
     * @param key The key to the bitmap in the textures. E.g. `example:custom/foo.png` would correspond to `assets/example/textures/custom/foo.png`. Needs to end with `.png`.
     * @param height The size of the bitmap. Defaults to 8.
     * @param ascent The vertical shift of the bitmap. **The ascent can not be bigger than the height**. Defaults to 7.
     * @param chars A list of characters for this bitmap to be used.
     */
    @Serializable
    public data class Bitmap(
        @SerialName("file")
        public var key: Key = Key("minecraft", "default"),
        public var height: Double = 8.0,
        public var ascent: Double = 7.0,
        public var chars: List<String> = listOf(),
        public val type: String = "bitmap",
    ) : FontProvider() {
        init {
            if (ascent > height) {
                throw IllegalArgumentException("Ascent $ascent can not be higher than the height of $height.")
            }
        }
    }

    /**
     * Defines the width of a character's glyph.
     * @param advances The advances of each character.
     */
    @Serializable
    public data class Space(
        public var advances: MutableMap<Char, Double> = mutableMapOf(),
        public val type: String = "space",
    ) : FontProvider()

    /**
     * A truetype font provider. Allows you to use a pre-forged truetype font.
     * @param key The name of the font. E.g. `example:custom_font.ttf`. Needs to end with `.ttf`.
     * @param shift Allows you to control the movement of the font horizontally and vertically (in that order).
     *              For shifting a font upwards, use e.g. `[0.0, 10.0]`. For shifting downwards, use e.g. `[0.0, -10.0]`.
     *              For shifting a font to the left, use e.g. `[10.0, 0.0]`. For shifting to the right, use e.g. `[-10.0, 0.0]`.
     * @param size The scale of the font.
     * @param oversample Resolution to render the font at.
     * @see net.radstevee.packed.core.font.FontProvider.Bitmap.height
     */
    @Serializable
    public data class Truetype(
        @SerialName("file")
        public var key: Key = Key("minecraft", "default"),
        public var shift: List<Double> = listOf(),
        public var size: Double = 0.0,
        public var oversample: Double = 0.0,
        public val type: String = "ttf",
    ) : FontProvider()

    /**
     * A reference font provider.
     * Includes a different provider. This allows you to include providers from other fonts or even resource packs.
     *
     * [See the Minecraft wiki](https://minecraft.wiki/w/Font#Reference_provider)
     * @param provider The font provider.
     */
    @Serializable
    public data class Reference(
        @SerialName("id")
        public var provider: Key = Key("minecraft", "default"),
        public val type: String = "reference",
    ) : FontProvider()
}
