package net.radstevee.packed.core.item.definition

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.item.definition.tint.Tint
import net.radstevee.packed.core.item.definition.tint.TintIdMapper
import net.radstevee.packed.core.key.Key

public class BasicItem(
    public val model: Key,
    public val tints: List<Tint> = listOf()
) : ItemDefinitionType {
    public companion object {
        public val CODEC: MapCodec<BasicItem> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Key.CODEC.fieldOf("model").forGetter(BasicItem::model),
                TintIdMapper.CODEC.listOf().optionalFieldOf("tints", emptyList()).forGetter(BasicItem::tints)
            ).apply(instance, ::BasicItem)
        }
    }

    override val typeCodec: MapCodec<out ItemDefinitionType> = CODEC
}
