package net.radstevee.packed.core.item.definition

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

/** An item model that can render multiple models. */
public class CompositeItem(
  /** The models */
  public val models: List<ItemDefinition>,
) : ItemDefinitionType {
  public companion object {
    /** The codec of this class. */
    public val CODEC: MapCodec<CompositeItem> = RecordCodecBuilder.mapCodec { instance ->
      instance.group(
        ItemDefinition.CODEC
          .listOf()
          .fieldOf("models")
          .forGetter(CompositeItem::models),
      ).apply(instance, ::CompositeItem)
    }
  }

  override val typeCodec: MapCodec<out ItemDefinitionType> = CODEC
}
