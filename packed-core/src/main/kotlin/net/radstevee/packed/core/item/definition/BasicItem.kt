package net.radstevee.packed.core.item.definition

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.item.definition.tint.Tint
import net.radstevee.packed.core.item.definition.tint.Tints
import net.radstevee.packed.core.key.Key

/** A basic item definition, rendering a model. */
public class BasicItem(
  /** The model key. */
  public val model: Key,
  /** The tints. */
  public val tints: List<Tint> = listOf(),
) : ItemDefinitionType {
  public companion object {
    /** The codec of this class. */
    public val CODEC: MapCodec<BasicItem> = RecordCodecBuilder.mapCodec { instance ->
      instance
        .group(
          Key.CODEC
            .fieldOf("model")
            .forGetter(BasicItem::model),
          Tints.CODEC
            .listOf()
            .optionalFieldOf("tints", emptyList())
            .forGetter(BasicItem::tints),
        ).apply(instance, ::BasicItem)
    }
  }

  override val typeCodec: MapCodec<out ItemDefinitionType> = CODEC
}
