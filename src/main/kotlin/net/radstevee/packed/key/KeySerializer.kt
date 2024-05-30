package net.radstevee.packed.key

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Key::class)
internal object KeySerializer : KSerializer<Key> {
    override fun serialize(encoder: Encoder, value: Key) {
        encoder.encodeString("${value.namespace}:${value.key}")
    }

    override fun deserialize(decoder: Decoder): Key {
        val (namespace, key) = decoder.decodeString().split(":")
        return Key(namespace, key)
    }
}