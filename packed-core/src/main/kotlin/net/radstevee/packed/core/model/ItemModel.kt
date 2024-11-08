@file:Suppress("unused")

package net.radstevee.packed.core.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import net.radstevee.packed.core.PACKED_LOGGER
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackElement
import net.radstevee.packed.core.util.Mat2x2i
import net.radstevee.packed.core.util.Vec3d
import net.radstevee.packed.core.util.Vec3i
import java.io.File

/**
 * Builds an item.
 * @param key The key.
 */
inline fun item(
    key: Key,
    block: ItemModel.Builder.() -> Unit,
) = ItemModel.Builder(key).apply(block).build()

/**
 * Represents an item model within a resource pack.
 * The code of this section is entirely undocumented. For more info, visit [the minecraft wiki](https://minecraft.wiki/w/Model).
 * Good luck.
 *
 * TODO: implement validation
 */
@Serializable
data class ItemModel(
    @Transient val key: Key = Key("", ""),
    val parent: String? = null,
    val display: ItemModelDisplay? = null,
    val textures: Map<String, Key>? = null,
    @SerialName("gui_light") val guiLight: String? = null,
    @SerialName("elements") val cubes: List<Cube>? = null,
    val overrides: List<OverrideCase>? = null,
) : ResourcePackElement {
    class Builder(
        val key: Key,
    ) {
        var parent: String? = null
        var display: ItemModelDisplay? = null
        var textures: MutableMap<String, Key>? = null
        val guiLight: String? = null
        var cubes: MutableList<Cube>? = null
        var overrides: MutableList<OverrideCase>? = null

        inline fun override(block: OverrideCase.Builder.() -> Unit) = override(OverrideCase.Builder().apply(block).build())

        fun override(case: OverrideCase) {
            if (overrides == null) overrides = mutableListOf()
            overrides!!.add(case)
        }

        fun particleTexture(texture: Key) {
            if (textures == null) textures = mutableMapOf()
            textures!!["particle"] = texture
        }

        fun layerTexture(
            layer: Int,
            texture: Key,
        ) {
            if (textures == null) textures = mutableMapOf()
            textures!!["layer$layer"] = texture
        }

        fun cubeTexture(
            cube: String,
            texture: Key,
        ) {
            if (textures == null) textures = mutableMapOf()
            textures!![cube] = texture
        }

        inline fun display(block: ItemModelDisplay.Builder.() -> Unit) {
            display = ItemModelDisplay.Builder().apply(block).build()
        }

        inline fun cubes(block: MutableList<Cube.Builder>.() -> Unit) {
            cubes = mutableListOf<Cube.Builder>().apply(block).map(Cube.Builder::build).toMutableList()
        }

        inline fun overrides(block: MutableList<OverrideCase.Builder>.() -> Unit) {
            overrides = mutableListOf<OverrideCase.Builder>().apply(block).map(OverrideCase.Builder::build).toMutableList()
        }

        fun build() = ItemModel(key, parent, display, textures, guiLight, cubes, overrides)
    }

    @Suppress("JSON_FORMAT_REDUNDANT")
    @OptIn(ExperimentalSerializationApi::class)
    fun json() =
        Json {
            prettyPrint = true

            explicitNulls = false
            classDiscriminatorMode = ClassDiscriminatorMode.NONE
            encodeDefaults = true
        }.encodeToString(this)

    override fun save(pack: ResourcePack) {
        key.createNamespace(pack)
        val file = File(pack.outputDir, "assets/${key.namespace}/models/${key.key}.json")
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeText(json())
        PACKED_LOGGER.info("Item model $key saved!")
    }
}

@Serializable
data class ItemModelDisplay(
    @SerialName("thirdperson_righthand") val thirdPersonRightHand: ItemModelDisplayPosition? = null,
    @SerialName("thirdperson_lefthand") val thirdPersonLeftHand: ItemModelDisplayPosition? = null,
    @SerialName("firstperson_righthand") val firstPersonRightHand: ItemModelDisplayPosition? = null,
    @SerialName("firstperson_lefthand") val firstPersonLeftHand: ItemModelDisplayPosition? = null,
    val gui: ItemModelDisplayPosition? = null,
    val head: ItemModelDisplayPosition? = null,
    val ground: ItemModelDisplayPosition? = null,
    val fixed: ItemModelDisplayPosition? = null,
) {
    class Builder {
        var thirdPersonRightHand: ItemModelDisplayPosition? = null
        var thirdPersonLeftHand: ItemModelDisplayPosition? = null
        var firstPersonRightHand: ItemModelDisplayPosition? = null
        var firstPersonLeftHand: ItemModelDisplayPosition? = null
        var gui: ItemModelDisplayPosition? = null
        var head: ItemModelDisplayPosition? = null
        var ground: ItemModelDisplayPosition? = null
        var fixed: ItemModelDisplayPosition? = null

        inline fun thirdPersonRightHand(block: ItemModelDisplayPosition.Builder.() -> Unit) {
            thirdPersonRightHand = ItemModelDisplayPosition.Builder().apply(block).build()
        }

        inline fun thirdPersonLeftHand(block: ItemModelDisplayPosition.Builder.() -> Unit) {
            thirdPersonLeftHand = ItemModelDisplayPosition.Builder().apply(block).build()
        }

        inline fun firstPersonRightHand(block: ItemModelDisplayPosition.Builder.() -> Unit) {
            firstPersonRightHand = ItemModelDisplayPosition.Builder().apply(block).build()
        }

        inline fun firstPersonLeftHand(block: ItemModelDisplayPosition.Builder.() -> Unit) {
            firstPersonLeftHand = ItemModelDisplayPosition.Builder().apply(block).build()
        }

        inline fun gui(block: ItemModelDisplayPosition.Builder.() -> Unit) {
            gui = ItemModelDisplayPosition.Builder().apply(block).build()
        }

        inline fun head(block: ItemModelDisplayPosition.Builder.() -> Unit) {
            head = ItemModelDisplayPosition.Builder().apply(block).build()
        }

        inline fun ground(block: ItemModelDisplayPosition.Builder.() -> Unit) {
            ground = ItemModelDisplayPosition.Builder().apply(block).build()
        }

        inline fun fixed(block: ItemModelDisplayPosition.Builder.() -> Unit) {
            fixed = ItemModelDisplayPosition.Builder().apply(block).build()
        }

        fun build() =
            ItemModelDisplay(thirdPersonRightHand, thirdPersonLeftHand, firstPersonRightHand, firstPersonLeftHand, gui, head, ground, fixed)
    }
}

@Serializable
data class ItemModelDisplayPosition(
    val rotation: Vec3d? = null,
    val translation: Vec3d? = null,
    val scale: Vec3d? = null,
) {
    class Builder {
        var rotation: Vec3d? = null
        var translation: Vec3d? = null
        var scale: Vec3d? = null

        fun build() =
            ItemModelDisplayPosition(
                rotation,
                translation,
                scale,
            )
    }
}

@Serializable
data class Cube(
    val from: Vec3i,
    val to: Vec3i,
    val rotation: CubeRotation? = null,
    val shade: Boolean = true,
    val faces: CubeFaces? = null,
) {
    class Builder {
        var from: Vec3i? = null
        var to: Vec3i? = null
        var rotation: CubeRotation? = null
        var shade: Boolean = true
        var faces: CubeFaces? = null

        inline fun faces(block: CubeFaces.Builder.() -> Unit) {
            faces = CubeFaces.Builder().apply(block).build()
        }

        fun build() = Cube(from ?: error("cube has no starting point"), to ?: error("cube has no ending point"), rotation, shade, faces)
    }
}

@Serializable
data class CubeFaces(
    val down: CubeFace? = null,
    val up: CubeFace? = null,
    val north: CubeFace? = null,
    val south: CubeFace? = null,
    val east: CubeFace? = null,
    val west: CubeFace? = null,
) {
    class Builder {
        var down: CubeFace? = null
        var up: CubeFace? = null
        var north: CubeFace? = null
        var south: CubeFace? = null
        var east: CubeFace? = null
        var west: CubeFace? = null

        inline fun down(block: CubeFace.Builder.() -> Unit) {
            down = CubeFace.Builder().apply(block).build()
        }

        inline fun up(block: CubeFace.Builder.() -> Unit) {
            up = CubeFace.Builder().apply(block).build()
        }

        inline fun north(block: CubeFace.Builder.() -> Unit) {
            north = CubeFace.Builder().apply(block).build()
        }

        inline fun south(block: CubeFace.Builder.() -> Unit) {
            south = CubeFace.Builder().apply(block).build()
        }

        inline fun east(block: CubeFace.Builder.() -> Unit) {
            east = CubeFace.Builder().apply(block).build()
        }

        inline fun west(block: CubeFace.Builder.() -> Unit) {
            west = CubeFace.Builder().apply(block).build()
        }

        fun build() = CubeFaces(down, up, north, south, east, west)
    }
}

@Serializable
data class CubeFace(
    val uv: Mat2x2i? = null,
    val texture: String? = null,
    @SerialName("cullface") val cullFace: String? = null,
    val rotation: Int? = null,
    @SerialName("tintindex") val tintIndex: Int? = null,
) {
    class Builder {
        var uv: Mat2x2i? = null
        var texture: String? = null
        var cullFace: String? = null
        var rotation: Int? = null
        var tintIndex: Int? = null

        fun build() =
            CubeFace(
                uv,
                texture,
                cullFace,
                rotation,
                tintIndex,
            )
    }
}

@Serializable
data class CubeRotation(
    val origin: Vec3d? = null,
    val axis: String? = null,
    val angle: Float? = null,
    val rescale: Boolean? = null,
) {
    class Builder {
        var origin: Vec3d? = null
        var axis: String? = null
        var angle: Float? = null
        var rescale: Boolean = false

        fun build() =
            CubeRotation(
                origin,
                axis,
                angle,
                rescale,
            )
    }
}

@Serializable
data class OverrideCase(
    val predicate: Map<String, Double>,
    val model: Key,
) {
    class Builder {
        var predicate: MutableMap<String, Double>? = null
        var model: Key? = null

        fun customModelData(target: Int) {
            if (predicate == null) predicate = mutableMapOf()
            predicate!!["custom_model_data"] = target.toDouble()
        }

        fun build() = OverrideCase(predicate ?: error("override has no predicate"), model ?: error("override has no model"))
    }
}
