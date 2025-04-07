package net.radstevee.packed.core.item.definition

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.encodeJson
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackElement
import net.radstevee.packed.core.packedLogger
import java.io.File
import kotlin.properties.Delegates

/** An item definition, defining a model for an item. */
public class ItemDefinition private constructor(
  /** The definition type. */
  public val type: ItemDefinitionType,
) : ResourcePackElement {
  public constructor(
    /** The key of this item. */
    item: Key,
    /** The definition type. */
    type: ItemDefinitionType,
  ) : this(type) {
    key = item
  }

  /** The key of this item. */
  public var key: Key by Delegates.notNull()

  override fun save(pack: ResourcePack) {
    key.createNamespace(pack)
    val file = File(pack.outputDir, "assets/${key.namespace}/items/${key.value}.json")
    val json = CODEC.encodeJson(this) ?: error("failed encoding item model")
    file.parentFile.mkdirs()
    file.writeText(json)
    packedLogger.info("Item definition $key saved!")
  }

  public companion object {
    /** The codec of this class. */
    public val CODEC: Codec<ItemDefinition> =
      RecordCodecBuilder.create { instance ->
        instance
          .group(
            ItemDefinitionTypes.CODEC
              .fieldOf("model")
              .forGetter(ItemDefinition::type),
          ).apply(instance, ::ItemDefinition)
      }
  }
}
