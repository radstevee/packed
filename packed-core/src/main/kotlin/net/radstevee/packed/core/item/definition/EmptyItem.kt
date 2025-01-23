package net.radstevee.packed.core.item.definition

import com.mojang.serialization.MapCodec

/** An empty item definition. */
public class EmptyItem : ItemDefinitionType {
    public companion object {
        /** The codec of this class. */
        public val CODEC: MapCodec<EmptyItem> = MapCodec.unit(::EmptyItem)
    }

    override val typeCodec: MapCodec<out ItemDefinitionType> = CODEC
}
