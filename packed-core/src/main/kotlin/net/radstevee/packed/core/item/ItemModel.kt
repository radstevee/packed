package net.radstevee.packed.core.item

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.encodeJson
import net.radstevee.packed.core.codec.nullableFieldOf
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackElement
import net.radstevee.packed.core.packedLogger
import net.radstevee.packed.core.util.Mat2x2d
import net.radstevee.packed.core.util.Vec3d
import java.io.File
import kotlin.properties.Delegates

/**
 * Builds an item model.
 * @param key The key of the item model.
 * @return The built item model.
 */
public inline fun itemModel(
  key: Key,
  block: ItemModel.Builder.() -> Unit,
): ItemModel = ItemModel.Builder(key).apply(block).build()

/** Represents an item model within a resource pack. */
public class ItemModel private constructor(
  /** The parent model. */
  public val parent: String?,
  /** The display options of this model. */
  public val display: ItemModelDisplay?,
  /** The textures of this model. */
  public val textures: Map<String, Key>?,
  /** The lighting options of this model. */
  public val guiLight: String?,
  /** The cubes contained in this model. */
  public val cubes: List<Cube>?,
  /** The override cases in this model. */
  public val overrides: List<OverrideCase>?,
) : ResourcePackElement {
  public constructor(
    /** The key of this item model. */
    key: Key,
    /** The parent model. */
    parent: String?,
    /** The display options of this model. */
    display: ItemModelDisplay?,
    /** The textures of this model. */
    textures: Map<String, Key>?,
    /** The lighting options of this model. */
    guiLight: String?,
    /** The cubes contained in this model. */
    cubes: List<Cube>?,
    /** The override cases in this model. */
    overrides: List<OverrideCase>?,
  ) : this(parent, display, textures, guiLight, cubes, overrides) {
    this.key = key
  }

  /** The key of this item model. */
  public var key: Key by Delegates.notNull()

  public companion object {
    /** The codec of this class. */
    public val CODEC: Codec<ItemModel> = RecordCodecBuilder.create { instance ->
      instance
        .group(
          Codec.STRING
            .nullableFieldOf("parent")
            .forGetter(ItemModel::parent),
          ItemModelDisplay.CODEC
            .nullableFieldOf("display")
            .forGetter(ItemModel::display),
          Codec
            .unboundedMap(Codec.STRING, Key.CODEC)
            .nullableFieldOf("textures")
            .forGetter(ItemModel::textures),
          Codec.STRING
            .nullableFieldOf("gui_light")
            .forGetter(ItemModel::guiLight),
          Cube.CODEC
            .listOf()
            .nullableFieldOf("elements")
            .forGetter(ItemModel::cubes),
          OverrideCase.CODEC
            .listOf()
            .nullableFieldOf("overrides")
            .forGetter(ItemModel::overrides),
        ).apply(instance, ::ItemModel)
    }
  }

  override fun save(pack: ResourcePack) {
    key.createNamespace(pack)
    val file = File(pack.outputDir, "assets/${key.namespace}/models/${key.value}.json")
    file.parentFile.mkdirs()
    file.createNewFile()
    file.writeText(CODEC.encodeJson(this) ?: error("failed encoding item model"))
    packedLogger.info("Item model $key saved!")
  }

  public class Builder(
    public val key: Key,
  ) {
    public var parent: String? = null
    public var display: ItemModelDisplay? = null
    public var textures: MutableMap<String, Key>? = null
    public var guiLight: String? = null
    public var cubes: MutableList<Cube>? = null
    public var overrides: MutableList<OverrideCase>? = null

    public inline fun override(block: OverrideCase.Builder.() -> Unit) {
      override(OverrideCase.Builder().apply(block).build())
    }

    public fun override(case: OverrideCase) {
      if (overrides == null) {
        overrides = mutableListOf()
      }
      overrides!!.add(case)
    }

    public fun particleTexture(texture: Key) {
      if (textures == null) {
        textures = mutableMapOf()
      }
      textures!!["particle"] = texture
    }

    public fun layerTexture(
      layer: Int,
      texture: Key,
    ) {
      if (textures == null) {
        textures = mutableMapOf()
      }
      textures!!["layer$layer"] = texture
    }

    public fun primaryTexture(texture: Key) {
      layerTexture(0, texture)
    }

    public fun cubeTexture(
      cube: String,
      texture: Key,
    ) {
      if (textures == null) {
        textures = mutableMapOf()
      }
      textures!![cube] = texture
    }

    public inline fun display(block: ItemModelDisplay.Builder.() -> Unit) {
      display = ItemModelDisplay.Builder().apply(block).build()
    }

    public inline fun cubes(block: MutableList<Cube.Builder>.() -> Unit) {
      cubes = mutableListOf<Cube.Builder>().apply(block).map(Cube.Builder::build).toMutableList()
    }

    public inline fun overrides(block: MutableList<OverrideCase.Builder>.() -> Unit) {
      overrides = mutableListOf<OverrideCase.Builder>().apply(block).map(OverrideCase.Builder::build).toMutableList()
    }

    public fun build(): ItemModel = ItemModel(key, parent, display, textures, guiLight, cubes, overrides)
  }
}

public data class ItemModelDisplay(
  public val thirdPersonRightHand: ItemModelDisplayPosition?,
  public val thirdPersonLeftHand: ItemModelDisplayPosition?,
  public val firstPersonRightHand: ItemModelDisplayPosition?,
  public val firstPersonLeftHand: ItemModelDisplayPosition?,
  public val gui: ItemModelDisplayPosition?,
  public val head: ItemModelDisplayPosition?,
  public val ground: ItemModelDisplayPosition?,
  public val fixed: ItemModelDisplayPosition?,
) {
  public companion object {
    public val CODEC: Codec<ItemModelDisplay> = RecordCodecBuilder.create { instance ->
      instance
        .group(
          ItemModelDisplayPosition.CODEC
            .nullableFieldOf("thirdperson_righthand")
            .forGetter(ItemModelDisplay::thirdPersonRightHand),
          ItemModelDisplayPosition.CODEC
            .nullableFieldOf("thirdperson_lefthand")
            .forGetter(ItemModelDisplay::thirdPersonLeftHand),
          ItemModelDisplayPosition.CODEC
            .nullableFieldOf("firstperson_righthand")
            .forGetter(ItemModelDisplay::firstPersonRightHand),
          ItemModelDisplayPosition.CODEC
            .nullableFieldOf("firstperson_lefthand")
            .forGetter(ItemModelDisplay::firstPersonLeftHand),
          ItemModelDisplayPosition.CODEC
            .nullableFieldOf("gui")
            .forGetter(ItemModelDisplay::gui),
          ItemModelDisplayPosition.CODEC
            .nullableFieldOf("head")
            .forGetter(ItemModelDisplay::head),
          ItemModelDisplayPosition.CODEC
            .nullableFieldOf("ground")
            .forGetter(ItemModelDisplay::ground),
          ItemModelDisplayPosition.CODEC
            .nullableFieldOf("fixed")
            .forGetter(ItemModelDisplay::fixed),
        ).apply(instance, ::ItemModelDisplay)
    }
  }

  public class Builder {
    public var thirdPersonRightHand: ItemModelDisplayPosition? = null
    public var thirdPersonLeftHand: ItemModelDisplayPosition? = null
    public var firstPersonRightHand: ItemModelDisplayPosition? = null
    public var firstPersonLeftHand: ItemModelDisplayPosition? = null
    public var gui: ItemModelDisplayPosition? = null
    public var head: ItemModelDisplayPosition? = null
    public var ground: ItemModelDisplayPosition? = null
    public var fixed: ItemModelDisplayPosition? = null

    public inline fun thirdPersonRightHand(block: ItemModelDisplayPosition.Builder.() -> Unit) {
      thirdPersonRightHand = ItemModelDisplayPosition.Builder().apply(block).build()
    }

    public inline fun thirdPersonLeftHand(block: ItemModelDisplayPosition.Builder.() -> Unit) {
      thirdPersonLeftHand = ItemModelDisplayPosition.Builder().apply(block).build()
    }

    public inline fun firstPersonRightHand(block: ItemModelDisplayPosition.Builder.() -> Unit) {
      firstPersonRightHand = ItemModelDisplayPosition.Builder().apply(block).build()
    }

    public inline fun firstPersonLeftHand(block: ItemModelDisplayPosition.Builder.() -> Unit) {
      firstPersonLeftHand = ItemModelDisplayPosition.Builder().apply(block).build()
    }

    public inline fun gui(block: ItemModelDisplayPosition.Builder.() -> Unit) {
      gui = ItemModelDisplayPosition.Builder().apply(block).build()
    }

    public inline fun head(block: ItemModelDisplayPosition.Builder.() -> Unit) {
      head = ItemModelDisplayPosition.Builder().apply(block).build()
    }

    public inline fun ground(block: ItemModelDisplayPosition.Builder.() -> Unit) {
      ground = ItemModelDisplayPosition.Builder().apply(block).build()
    }

    public inline fun fixed(block: ItemModelDisplayPosition.Builder.() -> Unit) {
      fixed = ItemModelDisplayPosition.Builder().apply(block).build()
    }

    public fun build(): ItemModelDisplay = ItemModelDisplay(thirdPersonRightHand, thirdPersonLeftHand, firstPersonRightHand, firstPersonLeftHand, gui, head, ground, fixed)
  }
}

public data class ItemModelDisplayPosition(
  public val rotation: Vec3d?,
  public val translation: Vec3d?,
  public val scale: Vec3d?,
) {
  public companion object {
    public val CODEC: Codec<ItemModelDisplayPosition> = RecordCodecBuilder.create { instance ->
      instance
        .group(
          Vec3d.CODEC
            .nullableFieldOf("rotation")
            .forGetter(ItemModelDisplayPosition::rotation),
          Vec3d.CODEC
            .nullableFieldOf("translation")
            .forGetter(ItemModelDisplayPosition::translation),
          Vec3d.CODEC
            .nullableFieldOf("scale")
            .forGetter(ItemModelDisplayPosition::scale),
        ).apply(instance, ::ItemModelDisplayPosition)
    }
  }

  public class Builder {
    public var rotation: Vec3d? = null
    public var translation: Vec3d? = null
    public var scale: Vec3d? = null

    public fun build(): ItemModelDisplayPosition = ItemModelDisplayPosition(
      rotation,
      translation,
      scale,
    )
  }
}

public data class Cube(
  public val from: Vec3d,
  public val to: Vec3d,
  public val rotation: CubeRotation?,
  public val shade: Boolean,
  public val faces: CubeFaces?,
) {
  public companion object {
    public val CODEC: Codec<Cube> = RecordCodecBuilder.create { instance ->
      instance
        .group(
          Vec3d.CODEC
            .fieldOf("from")
            .forGetter(Cube::from),
          Vec3d.CODEC
            .fieldOf("to")
            .forGetter(Cube::to),
          CubeRotation.CODEC
            .nullableFieldOf("rotation")
            .forGetter(Cube::rotation),
          Codec.BOOL
            .fieldOf("shade")
            .forGetter(Cube::shade),
          CubeFaces.CODEC
            .nullableFieldOf("faces")
            .forGetter(Cube::faces),
        ).apply(instance, ::Cube)
    }
  }

  public class Builder {
    public var from: Vec3d? = null
    public var to: Vec3d? = null
    public var rotation: CubeRotation? = null
    public var shade: Boolean = true
    public var faces: CubeFaces? = null

    public inline fun faces(block: CubeFaces.Builder.() -> Unit) {
      faces = CubeFaces.Builder().apply(block).build()
    }

    public inline fun rotation(block: CubeRotation.Builder.() -> Unit) {
      rotation = CubeRotation.Builder().apply(block).build()
    }

    public fun build(): Cube = Cube(from ?: error("cube has no starting point"), to ?: error("cube has no ending point"), rotation, shade, faces)
  }
}

public data class CubeFaces(
  public val down: CubeFace?,
  public val up: CubeFace?,
  public val north: CubeFace?,
  public val south: CubeFace?,
  public val east: CubeFace?,
  public val west: CubeFace?,
) {
  public companion object {
    public val CODEC: Codec<CubeFaces> = RecordCodecBuilder.create { instance ->
      instance
        .group(
          CubeFace.CODEC
            .nullableFieldOf("down")
            .forGetter(CubeFaces::down),
          CubeFace.CODEC
            .nullableFieldOf("up")
            .forGetter(CubeFaces::up),
          CubeFace.CODEC
            .nullableFieldOf("north")
            .forGetter(CubeFaces::north),
          CubeFace.CODEC
            .nullableFieldOf("south")
            .forGetter(CubeFaces::south),
          CubeFace.CODEC
            .nullableFieldOf("east")
            .forGetter(CubeFaces::east),
          CubeFace.CODEC
            .nullableFieldOf("west")
            .forGetter(CubeFaces::west),
        ).apply(instance, ::CubeFaces)
    }
  }

  public class Builder {
    public var down: CubeFace? = null
    public var up: CubeFace? = null
    public var north: CubeFace? = null
    public var south: CubeFace? = null
    public var east: CubeFace? = null
    public var west: CubeFace? = null

    public inline fun down(block: CubeFace.Builder.() -> Unit) {
      down = CubeFace.Builder().apply(block).build()
    }

    public inline fun up(block: CubeFace.Builder.() -> Unit) {
      up = CubeFace.Builder().apply(block).build()
    }

    public inline fun north(block: CubeFace.Builder.() -> Unit) {
      north = CubeFace.Builder().apply(block).build()
    }

    public inline fun south(block: CubeFace.Builder.() -> Unit) {
      south = CubeFace.Builder().apply(block).build()
    }

    public inline fun east(block: CubeFace.Builder.() -> Unit) {
      east = CubeFace.Builder().apply(block).build()
    }

    public inline fun west(block: CubeFace.Builder.() -> Unit) {
      west = CubeFace.Builder().apply(block).build()
    }

    public fun build(): CubeFaces = CubeFaces(down, up, north, south, east, west)
  }
}

public data class CubeFace(
  public val uv: Mat2x2d?,
  public val texture: String?,
  public val cullFace: String?,
  public val rotation: Int?,
  public val tintIndex: Int?,
) {
  public companion object {
    public val CODEC: Codec<CubeFace> = RecordCodecBuilder.create { instance ->
      instance
        .group(
          Mat2x2d.CODEC
            .nullableFieldOf("uv")
            .forGetter(CubeFace::uv),
          Codec.STRING
            .nullableFieldOf("texture")
            .forGetter(CubeFace::texture),
          Codec.STRING
            .nullableFieldOf("cullface")
            .forGetter(CubeFace::cullFace),
          Codec.INT
            .nullableFieldOf("rotation")
            .forGetter(CubeFace::rotation),
          Codec.INT
            .nullableFieldOf("tintindex")
            .forGetter(CubeFace::tintIndex),
        ).apply(instance, ::CubeFace)
    }
  }

  public class Builder {
    public var uv: Mat2x2d? = null
    public var texture: String? = null
    public var cullFace: String? = null
    public var rotation: Int? = null
    public var tintIndex: Int? = null

    public fun build(): CubeFace = CubeFace(
      uv,
      texture,
      cullFace,
      rotation,
      tintIndex,
    )
  }
}

public data class CubeRotation(
  public val origin: Vec3d?,
  public val axis: String?,
  public val angle: Float?,
  public val rescale: Boolean?,
) {
  public companion object {
    public val CODEC: Codec<CubeRotation> = RecordCodecBuilder.create { instance ->
      instance
        .group(
          Vec3d.CODEC
            .nullableFieldOf("origin")
            .forGetter(CubeRotation::origin),
          Codec.STRING
            .nullableFieldOf("axis")
            .forGetter(CubeRotation::axis),
          Codec.FLOAT
            .nullableFieldOf("angle")
            .forGetter(CubeRotation::angle),
          Codec.BOOL
            .nullableFieldOf("rescale")
            .forGetter(CubeRotation::rescale),
        ).apply(instance, ::CubeRotation)
    }
  }

  public class Builder {
    public var origin: Vec3d? = null
    public var axis: String? = null
    public var angle: Float? = null
    public var rescale: Boolean = false

    public fun build(): CubeRotation = CubeRotation(
      origin,
      axis,
      angle,
      rescale,
    )
  }
}

public data class OverrideCase(
  public val predicate: Map<String, Double>,
  public val model: Key,
) {
  public companion object {
    public val CODEC: Codec<OverrideCase> = RecordCodecBuilder.create { instance ->
      instance
        .group(
          Codec
            .unboundedMap(Codec.STRING, Codec.DOUBLE)
            .fieldOf("predicate")
            .forGetter(OverrideCase::predicate),
          Key.CODEC
            .fieldOf("model")
            .forGetter(OverrideCase::model),
        ).apply(instance, ::OverrideCase)
    }
  }

  public class Builder {
    public var predicate: MutableMap<String, Double>? = null
    public var model: Key? = null

    public fun customModelData(target: Int) {
      if (predicate == null) predicate = mutableMapOf()
      predicate!!["custom_model_data"] = target.toDouble()
    }

    public fun build(): OverrideCase = OverrideCase(
      predicate ?: error("override has no predicate"),
      model ?: error("override has no model"),
    )
  }
}
