@file:Suppress("PropertyName")

package net.radstevee.packed.pack

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
data class Pack(
    val pack_format: Int,
    val supported_formats: SupportedFormats?,
    val description: String
)

/**
 * Represents the `supported_formats` part in a `pack.mcmeta´.
 * Supported formats/versions for the resource pack.
 */
@Serializable
data class SupportedFormats(val min_inclusive: Int, val max_inclusive: Int)

/**
 * Represents a pack language.
 */
@Serializable
data class PackLanguage(val name: String, val region: String, val bidirectional: Boolean)

/**
 * Represents the `pack.mcmeta` file.
 */
@Serializable
data class ResourcePackMeta(
    val pack: Pack? = null,
    val language: PackLanguage? = null
) {
    @OptIn(ExperimentalSerializationApi::class)
    fun json(): String {
        @Suppress("JSON_FORMAT_REDUNDANT")
        return Json { prettyPrint = true; explicitNulls = false }.encodeToString(this)
    }

    companion object {
        fun init(format: PackFormat, description: String): ResourcePackMeta {
            return ResourcePackMeta(
                Pack(format.rev, null, description), null
            )
        }
    }
}