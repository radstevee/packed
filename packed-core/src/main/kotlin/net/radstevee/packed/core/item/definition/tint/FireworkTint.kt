package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.Codecs

/** A tint that is based on the average value of a firework explosion component. */
public class FireworkTint(
  /** The default color. */
  public val defaultColor: Int,
) : Tint {
  public companion object {
    public val CODEC: MapCodec<FireworkTint> = RecordCodecBuilder.mapCodec { instance ->
      instance
        .group(
          Codecs.RGB_COLOR
            .fieldOf("default")
            .forGetter(FireworkTint::defaultColor),
        ).apply(instance, ::FireworkTint)
    }
  }

  override val codec: MapCodec<out Tint> = CODEC
}
