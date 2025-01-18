package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.Codecs

public class FireworkTint(
    public val defaultColor: Int = -7697782,
) : Tint {
    public companion object {
        public val CODEC: MapCodec<FireworkTint> =
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        Codecs.RGB_COLOR.fieldOf("default").forGetter(FireworkTint::defaultColor),
                    ).apply(instance, ::FireworkTint)
            }
    }

    override val codec: MapCodec<out Tint> = CODEC
}
