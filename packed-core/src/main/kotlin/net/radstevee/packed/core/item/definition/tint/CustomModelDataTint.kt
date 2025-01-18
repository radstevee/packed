package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.Codecs

public class CustomModelDataTint(
    public val idx: Int,
    public val defaultColor: Int,
) : Tint {
    public companion object {
        public val CODEC: MapCodec<CustomModelDataTint> =
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        Codec.INT.optionalFieldOf("index", 0).forGetter(CustomModelDataTint::idx),
                        Codecs.RGB_COLOR.fieldOf("default").forGetter(CustomModelDataTint::defaultColor),
                    ).apply(instance, ::CustomModelDataTint)
            }
    }

    override val codec: MapCodec<out Tint> = CODEC
}
