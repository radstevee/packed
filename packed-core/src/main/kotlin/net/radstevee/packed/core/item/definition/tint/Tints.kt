package net.radstevee.packed.core.item.definition.tint

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.radstevee.packed.core.codec.IdMapper
import net.radstevee.packed.core.key.Key

/** ID mapper for tints. */
public object Tints {
    /** The ID mapper. */
    private val ID_MAPPER: IdMapper<Key, MapCodec<out Tint>> = IdMapper()

    /** The codec for a dispatched tint. */
    public val CODEC: Codec<Tint> = ID_MAPPER.codec(Key.CODEC).dispatch(Tint::codec) { it }

    private fun add(
        key: String,
        codec: MapCodec<out Tint>,
    ) {
        ID_MAPPER[Key.minecraft(key)] = codec
    }

    init {
        add("custom_model_data", CustomModelDataTint.CODEC)
        add("constant", ConstantTint.CODEC)
        add("dye", DyeTint.CODEC)
        add("grass", GrassColorTint.CODEC)
        add("firework", FireworkTint.CODEC)
        add("potion", PotionTint.CODEC)
        add("map_color", MapColorTint.CODEC)
        add("team", TeamColorTint.CODEC)
    }
}
