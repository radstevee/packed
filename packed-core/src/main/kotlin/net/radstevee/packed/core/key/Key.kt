package net.radstevee.packed.core.key

import kotlinx.serialization.Serializable
import net.radstevee.packed.core.pack.ResourcePack
import java.io.File

/**
 * Represents a key with a namespace. It will be serialized to `namespace:key`.
 * @param namespace The namespace. This can be minecraft, but can also be a custom one.
 * @param key The key. For example, `default` for the default font.
 */
@Serializable(with = KeySerializer::class)
data class Key(
    val namespace: String,
    val key: String,
) {
    override fun toString(): String = "$namespace:$key"

    fun createNamespace(pack: ResourcePack) {
        File(pack.outputDir, "assets/$namespace").mkdirs()
    }

    companion object {
        fun fromString(string: String): Key {
            val (namespace, key) = string.split(":")
            return Key(namespace, key)
        }
    }
}
