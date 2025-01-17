package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.Codecs

public class PotionTint(public val defaultColor: Int) : Tint {
    public companion object {
        public val CODEC: MapCodec<PotionTint> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codecs.RGB_COLOR.fieldOf("default").forGetter(PotionTint::defaultColor)
            ).apply(instance, ::PotionTint)
        }
    }

    override val codec: MapCodec<out Tint> = CODEC
}
