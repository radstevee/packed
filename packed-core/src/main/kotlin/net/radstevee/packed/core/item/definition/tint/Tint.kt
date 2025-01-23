package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.MapCodec

/** A tint that can be applied to an item definition. */
public interface Tint {
    /** The codec of this tint. */
    public val codec: MapCodec<out Tint>
}
