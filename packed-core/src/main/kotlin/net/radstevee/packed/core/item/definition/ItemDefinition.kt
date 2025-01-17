package net.radstevee.packed.core.item.definition

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.PACKED_LOGGER
import net.radstevee.packed.core.codec.encodeJson
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackElement
import java.io.File
import kotlin.properties.Delegates

public class ItemDefinition private constructor(public val type: ItemDefinitionType) : ResourcePackElement {
    public constructor(item: Key, type: ItemDefinitionType) : this(type) {
        key = item
    }

    public var key: Key by Delegates.notNull()

    override fun save(pack: ResourcePack) {
        key.createNamespace(pack)
        val file = File(pack.outputDir, "assets/${key.namespace}/items/${key.value}.json")
        val json = CODEC.encodeJson(this) ?: error("failed encoding item model")
        file.parentFile.mkdirs()
        file.writeText(json)
        PACKED_LOGGER.info("Item definition $key saved!")
    }

    public companion object {
        public val CODEC: Codec<ItemDefinition> = RecordCodecBuilder.create { instance ->
            instance.group(
                ItemDefinitionTypes.ITEM_DEF_CODEC
                    .fieldOf("model")
                    .forGetter(ItemDefinition::type)
            ).apply(instance, ::ItemDefinition)
        }
    }
}
