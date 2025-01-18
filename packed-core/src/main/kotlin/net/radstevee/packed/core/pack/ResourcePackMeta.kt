package net.radstevee.packed.core.pack

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.encodeJson
import net.radstevee.packed.core.codec.nullableFieldOf

/**
 * Represents the `pack` part in a `pack.mcmeta`.
 * Example:
 * ```json
 * {
 *     "pack": {
 *          "description": "Example pack"
 *     }
 * }
 * ```
 */
public data class Pack(
    val packFormat: Int,
    val supportedFormats: SupportedFormats?,
    val description: String?,
) {
    public companion object {
        public val CODEC: Codec<Pack> =
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        Codec.INT
                            .fieldOf("pack_format")
                            .forGetter(Pack::packFormat),
                        SupportedFormats.CODEC
                            .nullableFieldOf("supported_formats")
                            .forGetter(Pack::supportedFormats),
                        Codec.STRING
                            .nullableFieldOf("description")
                            .forGetter(Pack::description),
                    ).apply(instance, ::Pack)
            }
    }
}

/**
 * Represents the `supported_formats` part in a `pack.mcmeta`.
 * Supported formats/versions for the resource pack.
 */
public data class SupportedFormats(
    val minInclusive: Int,
    val maxInclusive: Int,
) {
    public companion object {
        public val CODEC: Codec<SupportedFormats> =
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        Codec.INT
                            .fieldOf("min_inclusive")
                            .forGetter(SupportedFormats::minInclusive),
                        Codec.INT
                            .fieldOf("max_inclusive")
                            .forGetter(SupportedFormats::maxInclusive),
                    ).apply(instance, ::SupportedFormats)
            }
    }
}

/**
 * Represents a pack language.
 */
public data class PackLanguage(
    val name: String,
    val region: String,
    val bidirectional: Boolean,
) {
    public companion object {
        public val CODEC: Codec<PackLanguage> =
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        Codec.STRING
                            .fieldOf("name")
                            .forGetter(PackLanguage::name),
                        Codec.STRING
                            .fieldOf("region")
                            .forGetter(PackLanguage::region),
                        Codec.BOOL
                            .fieldOf("bidirectional")
                            .forGetter(PackLanguage::bidirectional),
                    ).apply(instance, ::PackLanguage)
            }
    }
}

/**
 * Represents the `pack.mcmeta` file.
 */
public data class ResourcePackMeta(
    val pack: Pack? = null,
    val language: PackLanguage? = null,
) {
    public fun json(): String? = CODEC.encodeJson(this)

    public companion object {
        public val CODEC: Codec<ResourcePackMeta> =
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        Pack.CODEC.nullableFieldOf("pack").forGetter(ResourcePackMeta::pack),
                        PackLanguage.CODEC.nullableFieldOf("language").forGetter(ResourcePackMeta::language),
                    ).apply(instance, ::ResourcePackMeta)
            }

        /**
         * Creates a default resource pack meta from a format and description
         */
        public fun create(
            format: PackFormat,
            description: String?,
        ): ResourcePackMeta =
            ResourcePackMeta(
                Pack(format.rev, null, description),
                null,
            )
    }
}
