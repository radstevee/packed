package net.radstevee.packed.core.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.radstevee.packed.core.JSON
import net.radstevee.packed.core.PACKED_LOGGER
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackElement
import net.radstevee.packed.core.util.Mat2x2i
import net.radstevee.packed.core.util.Vec3d
import net.radstevee.packed.core.util.Vec3i
import java.io.File

/**
 * Builds an item model.
 * @param key The key of the item model.
 */
public inline fun itemModel(
    key: Key,
    block: ItemModel.Builder.() -> Unit,
): ItemModel = ItemModel.Builder(key).apply(block).build()

/**
 * Represents an item model within a resource pack.
 * For more info, visit [the minecraft wiki](https://minecraft.wiki/w/Model).
 *
 * TODO: implement validation
 */
@Serializable
public data class ItemModel(
    @Transient public val key: Key = Key("", ""),
    public val parent: String? = null,
    public val display: ItemModelDisplay? = null,
    public val textures: Map<String, Key>? = null,
    @SerialName("gui_light") public val guiLight: String? = null,
    @SerialName("elements") public val cubes: List<Cube>? = null,
    public val overrides: List<OverrideCase>? = null,
) : ResourcePackElement {
    public class Builder(
        public val key: Key,
    ) {
        public var parent: String? = null
        public var display: ItemModelDisplay? = null
        public var textures: MutableMap<String, Key>? = null
        public val guiLight: String? = null
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

    public fun json(): String = JSON.encodeToString(this)

    public override fun save(pack: ResourcePack) {
        key.createNamespace(pack)
        val file = File(pack.outputDir, "assets/${key.namespace}/models/${key.value}.json")
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeText(json())
        PACKED_LOGGER.info("Item model $key saved!")
    }
}

@Serializable
public data class ItemModelDisplay(
    @SerialName("thirdperson_righthand") public val thirdPersonRightHand: ItemModelDisplayPosition? = null,
    @SerialName("thirdperson_lefthand") public val thirdPersonLeftHand: ItemModelDisplayPosition? = null,
    @SerialName("firstperson_righthand") public val firstPersonRightHand: ItemModelDisplayPosition? = null,
    @SerialName("firstperson_lefthand") public val firstPersonLeftHand: ItemModelDisplayPosition? = null,
    public val gui: ItemModelDisplayPosition? = null,
    public val head: ItemModelDisplayPosition? = null,
    public val ground: ItemModelDisplayPosition? = null,
    public val fixed: ItemModelDisplayPosition? = null,
) {
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

        public fun build(): ItemModelDisplay =
            ItemModelDisplay(thirdPersonRightHand, thirdPersonLeftHand, firstPersonRightHand, firstPersonLeftHand, gui, head, ground, fixed)
    }
}

@Serializable
public data class ItemModelDisplayPosition(
    public val rotation: Vec3d? = null,
    public val translation: Vec3d? = null,
    public val scale: Vec3d? = null,
) {
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

@Serializable
public data class Cube(
    public val from: Vec3i,
    public val to: Vec3i,
    public val rotation: CubeRotation? = null,
    public val shade: Boolean = true,
    public val faces: CubeFaces? = null,
) {
    public class Builder {
        public var from: Vec3i? = null
        public var to: Vec3i? = null
        public var rotation: CubeRotation? = null
        public var shade: Boolean = true
        public var faces: CubeFaces? = null

        public inline fun faces(block: CubeFaces.Builder.() -> Unit) {
            faces = CubeFaces.Builder().apply(block).build()
        }

        public fun build(): Cube = Cube(from ?: error("cube has no starting point"), to ?: error("cube has no ending point"), rotation, shade, faces)
    }
}

@Serializable
public data class CubeFaces(
    public val down: CubeFace? = null,
    public val up: CubeFace? = null,
    public val north: CubeFace? = null,
    public val south: CubeFace? = null,
    public val east: CubeFace? = null,
    public val west: CubeFace? = null,
) {
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

@Serializable
public data class CubeFace(
    public val uv: Mat2x2i? = null,
    public val texture: String? = null,
    @SerialName("cullface") public val cullFace: String? = null,
    public val rotation: Int? = null,
    @SerialName("tintindex") public val tintIndex: Int? = null,
) {
    public class Builder {
        public var uv: Mat2x2i? = null
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

@Serializable
public data class CubeRotation(
    public val origin: Vec3d? = null,
    public val axis: String? = null,
    public val angle: Float? = null,
    public val rescale: Boolean? = null,
) {
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

@Serializable
public data class OverrideCase(
    public val predicate: Map<String, Double>,
    public val model: Key,
) {
    public class Builder {
        public var predicate: MutableMap<String, Double>? = null
        public var model: Key? = null

        public fun customModelData(target: Int) {
            if (predicate == null) predicate = mutableMapOf()
            predicate!!["custom_model_data"] = target.toDouble()
        }

        public fun build(): OverrideCase = OverrideCase(predicate ?: error("override has no predicate"), model ?: error("override has no model"))
    }
}
