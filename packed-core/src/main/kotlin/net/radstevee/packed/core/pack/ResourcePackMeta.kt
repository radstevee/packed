@file:Suppress("PropertyName")

package net.radstevee.packed.core.pack

import kotlinx.serialization.Serializable
import net.radstevee.packed.core.JSON

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
@Serializable
public data class Pack(
    val pack_format: Int,
    val supported_formats: SupportedFormats?,
    val description: String?,
)

/**
 * Represents the `supported_formats` part in a `pack.mcmeta´.
 * Supported formats/versions for the resource pack.
 */
@Serializable
public data class SupportedFormats(
    val min_inclusive: Int,
    val max_inclusive: Int,
)

/**
 * Represents a pack language.
 */
@Serializable
public data class PackLanguage(
    val name: String,
    val region: String,
    val bidirectional: Boolean,
)

/**
 * Represents the `pack.mcmeta` file.
 */
@Serializable
public data class ResourcePackMeta(
    val pack: Pack? = null,
    val language: PackLanguage? = null,
) {
    public fun json(): String = JSON.encodeToString(this)

    public companion object {
        /**
         * Creates a default resource pack meta from a format and description
         */
        public fun create(
            format: PackFormat,
            description: String?,
        ): ResourcePackMeta = ResourcePackMeta(
            Pack(format.rev, null, description),
            null,
        )
    }
}
