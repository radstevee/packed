package net.radstevee.packed.core.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.DoubleArraySerializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

private val intArray = IntArraySerializer()
private val doubleArray = DoubleArraySerializer()

@Serializable(with = Vec3iSerializer::class)
data class Vec3i(
    val x: Int,
    val y: Int,
    val z: Int,
)

private object Vec3iSerializer : KSerializer<Vec3i> {
    override val descriptor = PrimitiveSerialDescriptor("Vec3i", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Vec3i {
        val list = decoder.decodeSerializableValue(intArray)
        return Vec3i(list[0], list[1], list[2])
    }

    override fun serialize(
        encoder: Encoder,
        value: Vec3i,
    ) {
        val data =
            intArrayOf(
                value.x,
                value.y,
                value.z,
            )

        encoder.encodeSerializableValue(intArray, data)
    }
}

@Serializable(with = Vec3dSerializer::class)
data class Vec3d(
    val x: Double,
    val y: Double,
    val z: Double,
)

private object Vec3dSerializer : KSerializer<Vec3d> {
    override val descriptor = PrimitiveSerialDescriptor("Vec3d", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Vec3d {
        val list = decoder.decodeSerializableValue(doubleArray)
        return Vec3d(list[0], list[1], list[2])
    }

    override fun serialize(
        encoder: Encoder,
        value: Vec3d,
    ) {
        val data =
            doubleArrayOf(
                value.x,
                value.y,
                value.z,
            )

        encoder.encodeSerializableValue(doubleArray, data)
    }
}

@Serializable(with = Mat2x2iSerializer::class)
data class Mat2x2i(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,
)

private object Mat2x2iSerializer : KSerializer<Mat2x2i> {
    override val descriptor = PrimitiveSerialDescriptor("Mat2x2i", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Mat2x2i {
        val list = decoder.decodeSerializableValue(intArray)
        return Mat2x2i(list[0], list[1], list[2], list[3])
    }

    override fun serialize(
        encoder: Encoder,
        value: Mat2x2i,
    ) {
        val data =
            intArrayOf(
                value.x1,
                value.y1,
                value.x2,
                value.y2,
            )

        encoder.encodeSerializableValue(intArray, data)
    }
}

fun vec(
    x: Int,
    y: Int,
    z: Int,
) = Vec3i(x, y, z)

fun vec(
    x: Double,
    y: Double,
    z: Double,
) = Vec3d(x, y, z)

fun mat(
    x1: Int,
    y1: Int,
    x2: Int,
    y2: Int,
) = Mat2x2i(x1, y1, x2, y2)
