package net.radstevee.packed.core.util

import com.mojang.serialization.Codec
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.DoubleArraySerializer
import kotlinx.serialization.builtins.IntArraySerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.radstevee.packed.core.codec.fixedSize

private val intArraySerializer = IntArraySerializer()
private val doubleArraySerializer = DoubleArraySerializer()

@Serializable(with = Vec3iSerializer::class)
public data class Vec3i(
    val x: Int,
    val y: Int,
    val z: Int,
)

@Deprecated("Switch to DFU")
private object Vec3iSerializer : KSerializer<Vec3i> {
    override val descriptor = PrimitiveSerialDescriptor("Vec3i", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Vec3i {
        val list = decoder.decodeSerializableValue(intArraySerializer)
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

        encoder.encodeSerializableValue(intArraySerializer, data)
    }
}

@Serializable(with = Vec3dSerializer::class)
public data class Vec3d(
    val x: Double,
    val y: Double,
    val z: Double,
)

@Deprecated("Switch to DFU")
private object Vec3dSerializer : KSerializer<Vec3d> {
    override val descriptor = PrimitiveSerialDescriptor("Vec3d", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder): Vec3d {
        val list = decoder.decodeSerializableValue(doubleArraySerializer)
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

        encoder.encodeSerializableValue(doubleArraySerializer, data)
    }
}

public data class Vec3f(
    public val x: Float,
    public val y: Float,
    public val z: Float
) {
    public companion object {
        public val CODEC: Codec<Vec3f> = Codec.FLOAT.listOf().comapFlatMap(
            { floats ->
                fixedSize(floats, 3).map { floats -> Vec3f(floats[0], floats[1], floats[2]) }
            },
            { vec -> listOf(vec.x, vec.y, vec.z) }
        )
    }
}

@Serializable(with = Mat2x2iSerializer::class)
public data class Mat2x2i(
    val x1: Int,
    val y1: Int,
    val x2: Int,
    val y2: Int,
)

@Deprecated("Switch to DFU")
private object Mat2x2iSerializer : KSerializer<Mat2x2i> {
    override val descriptor = PrimitiveSerialDescriptor("Mat2x2i", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Mat2x2i {
        val list = decoder.decodeSerializableValue(intArraySerializer)
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

        encoder.encodeSerializableValue(intArraySerializer, data)
    }
}

/**
 * Constructs a 3-dimensional integer vector.
 */
public fun vec(
    x: Int,
    y: Int,
    z: Int,
): Vec3i = Vec3i(x, y, z)

/**
 * Constructs a 3-dimensional double vector.
 */
public fun vec(
    x: Double,
    y: Double,
    z: Double,
): Vec3d = Vec3d(x, y, z)

/**
 * Constructs a 3-dimensional float vector.
 */
public fun vec(
    x: Float,
    y: Float,
    z: Float
): Vec3f = Vec3f(x, y, z)

/**
 * Constructs a 2x2-dimensional integer matrix.
 */
public fun mat(
    x1: Int,
    y1: Int,
    x2: Int,
    y2: Int,
): Mat2x2i = Mat2x2i(x1, y1, x2, y2)
