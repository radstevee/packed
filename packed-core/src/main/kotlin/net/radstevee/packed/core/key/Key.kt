package net.radstevee.packed.core.key

import kotlinx.serialization.Serializable
import net.radstevee.packed.core.pack.ResourcePack
import java.io.File

/**
 * Represents a key with a namespace. It will be serialized to `namespace:key`.
 * @param namespace The namespace. This can be minecraft, but can also be a custom one.
 * @param value The key. For example, `default` for the default font.
 */
@Serializable(with = KeySerializer::class)
public data class Key(
    val namespace: String,
    val value: String,
) {
    override fun toString(): String = "$namespace:$value"

    public fun createNamespace(pack: ResourcePack) {
        File(pack.outputDir, "assets/$namespace").mkdirs()
    }

    public companion object {
        public fun of(string: String): Key {
            val (namespace, key) = string.split(":")
            return Key(namespace, key)
        }
    }
}
