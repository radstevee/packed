package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.Codecs

public class TeamColorTint(
    public val defaultColor: Int,
) : Tint {
    public companion object {
        public val CODEC: MapCodec<TeamColorTint> =
            RecordCodecBuilder.mapCodec { instance ->
                instance
                    .group(
                        Codecs.RGB_COLOR.fieldOf("default").forGetter(TeamColorTint::defaultColor),
                    ).apply(instance, ::TeamColorTint)
            }
    }

    override val codec: MapCodec<out Tint> = CODEC
}
