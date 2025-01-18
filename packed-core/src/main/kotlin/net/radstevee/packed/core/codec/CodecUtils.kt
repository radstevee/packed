package net.radstevee.packed.core.codec

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Encoder
import com.mojang.serialization.JsonOps
import com.mojang.serialization.MapCodec
import java.util.Optional
import kotlin.math.floor

internal fun <I, E> idResolverCodec(
    idCodec: Codec<I>,
    idToValue: (I) -> E,
    valueToId: (E) -> I,
): Codec<E> =
    idCodec.flatXmap({ id ->
        val value = idToValue(id)
        if (value == null) {
            DataResult.error { "unknown element with id: $id" }
        } else {
            DataResult.success(value)
        }
    }) { value ->
        val id = valueToId(value)
        if (id == null) {
            DataResult.error { "unknown element with id: $id" }
        } else {
            DataResult.success(id)
        }
    }

internal fun <T> fixedSize(
    list: List<T>,
    size: Int,
): DataResult<List<T>> {
    return if (list.size != size) {
        val err = { "list $list is not $size elements large" }

        return if (list.size >= size) {
            DataResult.error(err, list.subList(0, size))
        } else {
            DataResult.error(err)
        }
    } else {
        DataResult.success(list)
    }
}

internal fun as8BitChannel(value: Float): Int = floor(value * 255f).toInt()

internal fun colorFromFloat(
    a: Float,
    r: Float,
    g: Float,
    b: Float,
): Int {
    val a = as8BitChannel(a)
    val r = as8BitChannel(r)
    val g = as8BitChannel(g)
    val b = as8BitChannel(b)

    return a shl 24 or r shl 16 or g shl 8 or b
}

internal fun <A, T> Encoder<A>.encodeQuick(
    ops: DynamicOps<T>,
    input: A,
): T? =
    encodeStart(ops, input)
        .result()
        .orElse(null)

private val GSON: Gson = GsonBuilder().setPrettyPrinting().create()

internal fun <A> Encoder<A>.encodeJson(input: A): String? = encodeQuick(JsonOps.INSTANCE, input)?.let(GSON::toJson)

internal fun <A : Any> Codec<A>.nullableFieldOf(name: String): MapCodec<A?> =
    optionalFieldOf(name)
        .xmap(
            { it.orElse(null) },
            { Optional.ofNullable(it) },
        ).orElse(null)
