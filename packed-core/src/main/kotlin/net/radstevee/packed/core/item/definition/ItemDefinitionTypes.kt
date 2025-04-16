package net.radstevee.packed.core.item.definition

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.radstevee.packed.core.codec.IdMapper
import net.radstevee.packed.core.key.Key

/** ID mapper for item definition types. */
public object ItemDefinitionTypes {
  /** The ID mapper. */
  private val ID_MAPPER: IdMapper<Key, MapCodec<out ItemDefinitionType>> = IdMapper()

  /** The codec for a dispatched item definition type. */
  public val CODEC: Codec<ItemDefinitionType> = ID_MAPPER.codec(Key.CODEC).dispatch(ItemDefinitionType::typeCodec) { it }

  private fun add(
    key: String,
    codec: MapCodec<out ItemDefinitionType>,
  ) {
    ID_MAPPER[Key.minecraft(key)] = codec
  }

  init {
    add("empty", EmptyItem.CODEC)
    add("model", BasicItem.CODEC)
    add("composite", CompositeItem.CODEC)
    // TODO: more :)
  }
}
