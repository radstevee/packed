package net.radstevee.packed.core.font

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.key.Key

/**
 * Represents a provider within a font file.
 *
 * [See the Minecraft wiki for more information.](https://minecraft.wiki/w/Font#Providers)
 */
public interface FontProvider {
    public val providerCodec: MapCodec<out FontProvider>

    /**
     * A bitmap font provider. Allows you to add a coloured image to a font.
     * @param key The key to the bitmap in the textures. E.g. `example:custom/foo.png` would correspond to `assets/example/textures/custom/foo.png`. Needs to end with `.png`.
     * @param height The size of the bitmap. Defaults to 8.
     * @param ascent The vertical shift of the bitmap. **The ascent can not be bigger than the height**. Defaults to 7.
     * @param chars A list of characters for this bitmap to be used.
     */
    public data class Bitmap(
        public var key: Key = Key("minecraft", "default"),
        public var height: Double = 8.0,
        public var ascent: Double = 7.0,
        public var chars: List<String> = listOf(),
    ) : FontProvider {
        init {
            if (ascent > height) {
                throw IllegalArgumentException("Ascent $ascent can not be higher than the height of $height.")
            }
        }

        public companion object {
            public val CODEC: MapCodec<Bitmap> =
                RecordCodecBuilder.mapCodec { instance ->
                    instance
                        .group(
                            Key.CODEC
                                .fieldOf("key")
                                .forGetter(Bitmap::key),
                            Codec.DOUBLE
                                .fieldOf("height")
                                .forGetter(Bitmap::height),
                            Codec.DOUBLE
                                .fieldOf("ascent")
                                .forGetter(Bitmap::ascent),
                            Codec.STRING
                                .listOf()
                                .fieldOf("chars")
                                .forGetter(Bitmap::chars),
                        ).apply(instance, ::Bitmap)
                }
        }

        override val providerCodec: MapCodec<out FontProvider> = CODEC
    }

    /**
     * Defines the width of a character's glyph.
     * @param advances The advances of each character.
     */
    public data class Space(
        public var advances: Map<String, Double> = mapOf(),
    ) : FontProvider {
        public companion object {
            public val CODEC: MapCodec<Space> =
                RecordCodecBuilder.mapCodec { instance ->
                    instance
                        .group(
                            Codec
                                .unboundedMap(Codec.STRING, Codec.DOUBLE)
                                .fieldOf("advances")
                                .forGetter(Space::advances),
                        ).apply(instance, ::Space)
                }
        }

        override val providerCodec: MapCodec<out FontProvider> = CODEC
    }

    /**
     * A truetype font provider. Allows you to use a precompiled truetype font.
     *
     * For shifting a font upwards, use e.g. `[0.0, 10.0]`. For shifting downwards, use e.g. `[0.0, -10.0]`.
     *
     * For shifting a font to the left, use e.g. `[10.0, 0.0]`. For shifting to the right, use e.g. `[-10.0, 0.0]`.
     *
     * @param key The name of the font. E.g. `example:custom_font.ttf`. Needs to end with `.ttf`.
     * @param shift Allows you to control the movement of the font horizontally and vertically (in that order).
     * @param size The scale of the font.
     * @param oversample Resolution to render the font at.
     * @see net.radstevee.packed.core.font.FontProvider.Bitmap.height
     */
    public data class Truetype(
        public var key: Key = Key("minecraft", "default"),
        public var shift: List<Double> = listOf(),
        public var size: Double = 0.0,
        public var oversample: Double = 0.0,
    ) : FontProvider {
        public companion object {
            public val CODEC: MapCodec<Truetype> =
                RecordCodecBuilder.mapCodec { instance ->
                    instance
                        .group(
                            Key.CODEC
                                .fieldOf("file")
                                .forGetter(Truetype::key),
                            Codec.DOUBLE
                                .listOf()
                                .fieldOf("shift")
                                .forGetter(Truetype::shift),
                            Codec.DOUBLE
                                .fieldOf("size")
                                .forGetter(Truetype::size),
                            Codec.DOUBLE
                                .fieldOf("oversample")
                                .forGetter(Truetype::oversample),
                        ).apply(instance, ::Truetype)
                }
        }

        override val providerCodec: MapCodec<out FontProvider> = CODEC
    }

    /**
     * A reference font provider.
     * Includes a different provider. This allows you to include providers from other fonts or even resource packs.
     *
     * [See the Minecraft wiki](https://minecraft.wiki/w/Font#Reference_provider)
     * @param provider The font provider.
     */
    public data class Reference(
        public var provider: Key = Key("minecraft", "default"),
        public val type: String = "reference",
    ) : FontProvider {
        public companion object {
            public val CODEC: MapCodec<Reference> =
                RecordCodecBuilder.mapCodec { instance ->
                    instance
                        .group(
                            Key.CODEC
                                .fieldOf("id")
                                .forGetter(Reference::provider),
                        ).apply(instance, ::Reference)
                }
        }

        override val providerCodec: MapCodec<out FontProvider> = CODEC
    }

    public companion object {
        /**
         * Builds a bitmap font provider.
         * @return The font provider.
         */
        public inline fun bitmap(block: Bitmap.() -> Unit): Bitmap = Bitmap().apply(block)

        /**
         * Builds a TTF font provider.
         * @return The font provider.
         */
        public inline fun ttf(block: Truetype.() -> Unit): Truetype = Truetype().apply(block)

        /**
         * Builds a space font provider.
         * @return The font provider.
         */
        public inline fun space(block: Space.() -> Unit): Space = Space().apply(block)

        /**
         * Builds a reference font provider.
         * @return The font provider.
         */
        public inline fun reference(block: Reference.() -> Unit): Reference = Reference().apply(block)
    }
}
