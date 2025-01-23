package net.radstevee.packed.core.item.definition

import com.mojang.serialization.MapCodec

/** A type of item definition. */
public interface ItemDefinitionType {
    /** The codec for this type. */
    public val typeCodec: MapCodec<out ItemDefinitionType>
}
