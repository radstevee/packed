package net.radstevee.packed.namespace

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.radstevee.packed.Key

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Key::class)
internal object KeyableSerializer : KSerializer<Key> {
    override fun serialize(encoder: Encoder, value: Key) {
        encoder.encodeString("${value.namespace}:${value.key}")
    }

    override fun deserialize(decoder: Decoder): Key {
        val (namespace, key) = decoder.decodeString().split(":")
        return Key(namespace, key)
    }
}