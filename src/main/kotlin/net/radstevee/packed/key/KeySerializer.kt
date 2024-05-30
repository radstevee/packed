package net.radstevee.packed.key

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object KeySerializer : KSerializer<Key> {
    override fun serialize(encoder: Encoder, value: Key) {
        encoder.encodeString("${value.namespace}:${value.key}")
    }

    override val descriptor = PrimitiveSerialDescriptor("Key", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Key {
        val (namespace, key) = decoder.decodeString().split(":")
        return Key(namespace, key)
    }
}