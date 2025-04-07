package net.radstevee.packed.core.sound

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.UnboundedMapCodec
import net.radstevee.packed.core.codec.encodeJson
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackElement
import net.radstevee.packed.core.packedLogger
import java.io.File
import kotlin.properties.Delegates

/** A list of sounds in a namespace, akin to a sounds.json. */
public class SoundList(
  /** The sound events in this sound list. */
  public var soundEvents: MutableList<SoundEvent>,
) : ResourcePackElement {
  public constructor(
    /** The namespace of this sound list. */
    namespace: String,
    /** The sound events in this sound list. */
    soundEvents: MutableList<SoundEvent> = mutableListOf(),
  ) : this(soundEvents) {
    this.namespace = namespace
  }

  /** The namespace of this sound list. */
  public var namespace: String by Delegates.notNull()

  /**
   * Adds a sound event to this sound list.
   * @param soundEvent The sound event.
   */
  public fun add(soundEvent: SoundEvent): SoundEvent = soundEvent.also(soundEvents::add)

  /**
   * Adds a basic sound event to this sound list.
   * @param key The key of the sound.
   */
  public fun add(key: Key): SoundEvent = add(SoundEvent(key))

  /**
   * Builds and adds a sound event to this sound list.
   * @param key The key of the sound. Can be null if you would like to set it in the builder.
   */
  public fun add(
    key: Key? = null,
    block: SoundEvent.() -> Unit,
  ): SoundEvent = add(SoundEvent.sound(key ?: Key.minecraft(""), block))

  public companion object {
    /** The codec of this class. */
    public val CODEC: UnboundedMapCodec<String, SoundEvent> = Codec.unboundedMap(Codec.STRING, SoundEvent.CODEC)
  }

  override fun save(pack: ResourcePack) {
    val file = File(pack.outputDir, "assets/$namespace/sounds.json")
    file.parentFile.mkdirs()
    val json =
      CODEC.encodeJson(soundEvents.associateBy { sound -> sound.key.value.removePrefix(".ogg") })
        ?: error("failed encoding sounds")
    file.parentFile.mkdirs()
    file.writeText(json)
    packedLogger.info("Sound list for namespace $namespace saved!")
  }
}
