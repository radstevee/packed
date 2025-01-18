package net.radstevee.packed.core.codec

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mojang.serialization.Codec

internal class IdMapper<I, V> {
    private val idToValue: BiMap<I, V> = HashBiMap.create()

    internal fun codec(idCodec: Codec<I>): Codec<V> {
        val valueToId = idToValue.inverse()

        return idResolverCodec(idCodec, idToValue::getValue, valueToId::getValue)
    }

    internal operator fun set(
        id: I,
        value: V,
    ) {
        idToValue[id] = value
    }
}
