package net.radstevee.packed.core.sound

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.nullableFieldOf
import net.radstevee.packed.core.key.Key
import kotlin.properties.Delegates

public class SoundEvent private constructor(
    public val replaceVanilla: Boolean = true,
    public val subtitle: String? = null,
    public val soundSet: List<Key> = listOf(),
) {
    public constructor(
        key: Key,
        replaceVanilla: Boolean = true,
        subtitle: String? = null,
        soundSet: List<Key> = listOf(key),
    ) : this(replaceVanilla, subtitle, soundSet) {
        this.key = key
    }

    public var key: Key by Delegates.notNull()

    public companion object {
        public val CODEC: Codec<SoundEvent> =
            RecordCodecBuilder.create { instance ->
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
    }
}
