package net.radstevee.packed.core.sound

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.nullableFieldOf
import net.radstevee.packed.core.key.Key
import kotlin.properties.Delegates

/** A sound event represents a set of sounds that can play when a sound event is started. */
public class SoundEvent(
  /** Whether this sound should replace vanilla's sound. */
  public var replaceVanilla: Boolean = true,
  /** The translation key of the subtitle that should be displayed. */
  public var subtitle: String? = null,
  /** The sound set of this sound event. */
  public var soundSet: MutableList<Key> = mutableListOf(),
) {
  public constructor(
    /** The key of this sound event. */
    key: Key,
    /** Whether this sound should replace vanilla's sound. */
    replaceVanilla: Boolean = true,
    /** The translation key of the subtitle that should be displayed. */
    subtitle: String? = null,
    /** The sound set of this sound event. */
    soundSet: MutableList<Key> = mutableListOf(key),
  ) : this(replaceVanilla, subtitle, soundSet) {
    this.key = key
  }

  /**
   * Adds a sound to this event's sound set.
   * @param key The sound key.
   */
  public fun addSound(key: Key) {
    soundSet.add(key)
  }

  /** The key of this sound event. */
  public var key: Key by Delegates.notNull()

  public companion object {
    /** The codec of this class. */
    public val CODEC: Codec<SoundEvent> = RecordCodecBuilder.create { instance ->
      instance
        .group(
          Codec.BOOL
            .fieldOf("replace")
            .forGetter(SoundEvent::replaceVanilla),
          Codec.STRING
            .nullableFieldOf("subtitle")
            .forGetter(SoundEvent::subtitle),
          Key.CODEC
            .listOf()
            .fieldOf("sounds")
            .forGetter(SoundEvent::soundSet),
        ).apply(instance, ::SoundEvent)
    }

    public inline fun sound(
      key: Key,
      block: SoundEvent.() -> Unit,
    ): SoundEvent = SoundEvent(key).apply(block)
  }
}
