package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.Codecs

/** A tint that is based on the map color component. */
public class MapColorTint(
  /** The default color. */
  public val defaultColor: Int,
) : Tint {
  public companion object {
    /** The codec of this class. */
    public val CODEC: MapCodec<MapColorTint> = RecordCodecBuilder.mapCodec { instance ->
      instance
        .group(
          Codecs.RGB_COLOR
            .fieldOf("default")
            .forGetter(MapColorTint::defaultColor),
        ).apply(instance, ::MapColorTint)
    }
  }

  override val codec: MapCodec<out Tint> = CODEC
}
