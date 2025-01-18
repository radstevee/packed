package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

public class GrassColorTint(
    public val temperature: Float,
    public val downfall: Float,
) : Tint {
    public companion object {
        public val CODEC: MapCodec<GrassColorTint> =
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        Codec.FLOAT.fieldOf("temperature").forGetter(GrassColorTint::temperature),
                        Codec.FLOAT.fieldOf("downfall").forGetter(GrassColorTint::downfall),
                    ).apply(instance, ::GrassColorTint)
            }
    }

    override val codec: MapCodec<out Tint> = CODEC
}
