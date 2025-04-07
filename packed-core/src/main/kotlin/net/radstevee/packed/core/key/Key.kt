package net.radstevee.packed.core.key

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.radstevee.packed.core.pack.ResourcePack
import java.io.File

/**
 * Represents a key with a namespace. It will be serialized to `namespace:key`.
 */
public data class Key(
  /** The namespace. */
  val namespace: String,
  /** The value (aka path). */
  val value: String,
) {
  override fun toString(): String = "$namespace:$value"

  /**
   * Saves this key's namespace to a resource pack.
   * @param pack The resource pack.
   */
  public fun createNamespace(pack: ResourcePack) {
    File(pack.outputDir, "assets/$namespace").mkdirs()
  }

  public companion object {
    /**
     * Attempts to read a key from a string. This may throw exceptions.
     * @param string The input string.
     * @return The read key.
     */
    public fun of(string: String): Key {
      val (namespace, key) = string.split(":")
      return Key(namespace, key)
    }

    /**
     * Creates a key in the minecraft namespace.
     * @param value The value of the key.
     * @return The created key.
     */
    public fun minecraft(value: String): Key = Key("minecraft", value)

    /**
     * Attempts to read a key from an input string, returning an error if it failed.
     * @param string The input string.
     * @return The result.
     */
    public fun read(string: String): DataResult<Key> = runCatching {
      DataResult.success(of(string))
    }.getOrElse { exception -> DataResult.error { "not a valid key: $string: ${exception.message}" } }

    /** The codec of this class. */
    public val CODEC: Codec<Key> = Codec.STRING.comapFlatMap(::read, Key::toString).stable()
  }
}
