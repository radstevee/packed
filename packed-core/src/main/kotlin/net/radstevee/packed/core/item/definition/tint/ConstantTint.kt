package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.Codecs

/** A tint that is always constant. */
public class ConstantTint(
    /** The color. */
    public val value: Int,
) : Tint {
    public companion object {
        /** The codec of this class. */
        public val CODEC: MapCodec<ConstantTint> =
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        Codecs.RGB_COLOR
                            .fieldOf("value")
                            .forGetter(ConstantTint::value),
                    ).apply(instance, ::ConstantTint)
            }
    }

    override val codec: MapCodec<out Tint> = CODEC
}
