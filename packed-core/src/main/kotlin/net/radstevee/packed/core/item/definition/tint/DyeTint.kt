package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.Codecs

public class DyeTint(
    public val defaultColor: Int,
) : Tint {
    public companion object {
        public val CODEC: MapCodec<DyeTint> =
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        Codecs.RGB_COLOR.fieldOf("default").forGetter(DyeTint::defaultColor),
                    ).apply(instance, ::DyeTint)
            }
    }

    override val codec: MapCodec<out Tint> = CODEC
}
