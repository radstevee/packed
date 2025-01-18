package net.radstevee.packed.core.item.definition

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.radstevee.packed.core.codec.IdMapper
import net.radstevee.packed.core.key.Key

public object ItemDefinitionTypes {
    private val ID_MAPPER: IdMapper<Key, MapCodec<out ItemDefinitionType>> = IdMapper()
    public val ITEM_DEF_CODEC: Codec<ItemDefinitionType> = ID_MAPPER.codec(Key.CODEC).dispatch(ItemDefinitionType::typeCodec) { it }

    private fun add(
        key: String,
        codec: MapCodec<out ItemDefinitionType>,
    ) {
        ID_MAPPER[Key.minecraft(key)] = codec
    }

    init {
        add("empty", EmptyItem.CODEC)
        add("model", BasicItem.CODEC)
        // TODO: more :)
    }
}
