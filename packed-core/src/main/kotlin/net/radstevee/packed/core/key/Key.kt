package net.radstevee.packed.core.key

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.radstevee.packed.core.pack.ResourcePack
import java.io.File

/**
 * Represents a key with a namespace. It will be serialized to `namespace:key`.
 * @param namespace The namespace. This can be minecraft, but can also be a custom one.
 * @param value The key. For example, `default` for the default font.
 */
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

        public fun minecraft(value: String): Key {
            return Key("minecraft", value)
        }

        internal fun read(string: String): DataResult<Key> {
            return runCatching {
                DataResult.success(of(string))
            }.getOrElse { exception -> DataResult.error { "not a valid key: $string: ${exception.message}" } }
        }

        public val CODEC: Codec<Key> = Codec.STRING.comapFlatMap(::read, Key::toString).stable()
    }
}
