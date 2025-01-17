package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.MapCodec

public interface Tint {
    public val codec: MapCodec<out Tint>
}
