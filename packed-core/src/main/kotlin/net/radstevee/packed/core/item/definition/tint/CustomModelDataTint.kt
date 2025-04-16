package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.Codecs
import net.radstevee.packed.core.codec.nullableFieldOf

/** A tint that is based on the custom model data value `colors` list. */
public class CustomModelDataTint(
  /** The index of the color in the `colors` list. Defaults to zero. */
  public val idx: Int? = 0,
  /** The default color. */
  public val defaultColor: Int,
) : Tint {
  public companion object {
    /** The codec of this class. */
    public val CODEC: MapCodec<CustomModelDataTint> = RecordCodecBuilder.mapCodec { instance ->
      instance
        .group(
          Codec.INT
            .nullableFieldOf("index")
            .forGetter(CustomModelDataTint::idx),
          Codecs.RGB_COLOR
            .fieldOf("default")
            .forGetter(CustomModelDataTint::defaultColor),
        ).apply(instance, ::CustomModelDataTint)
    }
  }

  override val codec: MapCodec<out Tint> = CODEC
}
