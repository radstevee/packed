package net.radstevee.packed.core.sound

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.UnboundedMapCodec
import net.radstevee.packed.core.codec.encodeJson
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackElement
import net.radstevee.packed.core.packedLogger
import java.io.File
import kotlin.properties.Delegates

public class SoundList private constructor(
    public var soundEvents: List<SoundEvent>,
) : ResourcePackElement {
    public constructor(namespace: String, soundEvents: List<SoundEvent> = listOf()) : this(soundEvents) {
        this.namespace = namespace
    }

    public var namespace: String by Delegates.notNull()

    public companion object {
        public val CODEC: UnboundedMapCodec<String, SoundEvent> = Codec.unboundedMap(Codec.STRING, SoundEvent.CODEC)
    }

    override fun save(pack: ResourcePack) {
        val file = File(pack.outputDir, "assets/$namespace/sounds.json")
        file.parentFile.mkdirs()
        val json =
            CODEC.encodeJson(soundEvents.associateBy { sound -> sound.key.value.removePrefix(".ogg") }) ?: error("failed encoding sounds")
        file.parentFile.mkdirs()
        file.writeText(json)
        packedLogger.info("Sound list for namespace $namespace saved!")
    }
}
