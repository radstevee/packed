package net.radstevee.packed.font

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.radstevee.packed.key.Key

/**
 * A font provider. A font provider is specified in an array of them in a font file.
 */
@Serializable
sealed class FontProvider {
    /**
     * A bitmap font provider. Allows you to add a coloured image to a font.
     * @param key The key to the bitmap in the textures. E.g. `example:custom/foo.png` would correspond to `assets/example/textures/custom/foo.png`. Needs to end with `.png`.
     * @param height The size of the bitmap. Defaults to 8.
     * @param ascent The vertical shift of the bitmap. **The ascent can not be bigger than the height**. Defaults to 7.
     * @param chars A list of characters for this bitmap to be used.
     */
    @Serializable
    data class BITMAP(
        @SerialName("file") var key: Key = Key("minecraft", "default"),
        var height: Double = 8.0,
        var ascent: Double = 7.0,
        var chars: List<String> = listOf(),
        val type: String = "bitmap"
    ) : FontProvider() {
        init {
            if (ascent > height) {
                throw IllegalArgumentException("Ascent $ascent can not be higher than the height of $height.")
            }
        }
    }

    @Serializable
    data object SPACE : FontProvider()

    /**
     * A truetype font provider. Allows you to use a pre-forged truetype font.
     * @param key The name of the font. E.g. `example:custom_font.ttf`. Needs to end with `.ttf`.
     * @param shift Allows you to control the movement of the font horizontally and vertically (in that order).
     *              For shifting a font upwards, use e.g. `[0.0, 10.0]`. For shifting downwards, use e.g. `[0.0, -10.0]`.
     *              For shifting a font to the left, use e.g. `[10.0, 0.0]`. For shifting to the right, use e.g. `[-10.0, 0.0]`.
     * @param size The scale of the font.
     * @param oversample Resolution to render the font at.
     * @see net.radstevee.packed.font.FontProvider.BITMAP.height
     */
    @Serializable
    data class TRUETYPE(
        @SerialName("file") var key: Key = Key("minecraft", "default"),
        var shift: List<Double> = listOf(),
        var size: Double = 0.0,
        var oversample: Double = 0.0,
        @SerialName("type") val type: String = "ttf"
    ) : FontProvider() {
        override fun toString(): String {
            return super.toString()
        }
    }

    @Serializable
    data object REFERENCE : FontProvider()
}
