package net.radstevee.packed.core.key

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public object KeySerializer : KSerializer<Key> {
    override fun serialize(
        encoder: Encoder,
        value: Key,
    ) {
        encoder.encodeString("${value.namespace}:${value.value}")
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Key", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Key = Key.of(decoder.decodeString())
}
