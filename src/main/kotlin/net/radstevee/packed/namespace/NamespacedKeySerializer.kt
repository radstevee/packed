package net.radstevee.packed.namespace

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = NamespacedKey::class)
internal object NamespacedKeySerializer : KSerializer<NamespacedKey> {
    override fun serialize(encoder: Encoder, value: NamespacedKey) {
        encoder.encodeString("${value.namespace}:${value.key}")
    }

    override fun deserialize(decoder: Decoder): NamespacedKey {
        val (namespace, key) = decoder.decodeString().split(":")
        return NamespacedKey(namespace, key)
    }
}