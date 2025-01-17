package net.radstevee.packed.core.item.definition

import com.mojang.serialization.MapCodec

public class EmptyItem : ItemDefinitionType {
    public companion object {
        public val CODEC: MapCodec<EmptyItem> = MapCodec.unit(::EmptyItem)
    }

    override val typeCodec: MapCodec<out ItemDefinitionType> = CODEC
}
