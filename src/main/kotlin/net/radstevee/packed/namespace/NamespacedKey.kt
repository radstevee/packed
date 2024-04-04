package net.radstevee.packed.namespace

import kotlinx.serialization.Serializable
import net.radstevee.packed.pack.ResourcePack
import java.io.File

/**
 * Represents a namespaced key. It will be serialized to `namespace:key`.
 * @param namespace The namespace. This can be minecraft, but can also be a custom one.
 * @param key The key. For example, `default` for the default font.
 */
@Serializable(with = NamespacedKeySerializer::class)
data class NamespacedKey(val namespace: String, val key: String) {
    override fun toString(): String {
        return "$namespace:$key"
    }

    fun createNamespace(pack: ResourcePack) {
        File(pack.outputDir, "assets/$namespace").mkdirs()
    }
}