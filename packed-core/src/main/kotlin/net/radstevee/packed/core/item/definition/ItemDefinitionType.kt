package net.radstevee.packed.core.item.definition

import com.mojang.serialization.MapCodec

public interface ItemDefinitionType {
    public val typeCodec: MapCodec<out ItemDefinitionType>
}
