package net.radstevee.packed.core.codec

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mojang.serialization.Codec

/** An ID mapping between an ID and a value. */
public class IdMapper<
    /** The ID type. */
    I,
    /** The value type. */
    V,
> {
    private val idToValue: BiMap<I, V> = HashBiMap.create()

    /** Creates an ID mapping codec for the given ID codec.
     * @param idCodec The ID codec.
     * @return The mapped codec.
     */
    public fun codec(idCodec: Codec<I>): Codec<V> {
        val valueToId = idToValue.inverse()

        return idResolverCodec(idCodec, idToValue::getValue, valueToId::getValue)
    }

    /**
     * Sets a value in the ID-value map.
     * @param id The ID.
     * @param value The value.
     */
    public operator fun set(
        id: I,
        value: V,
    ) {
        idToValue[id] = value
    }
}
