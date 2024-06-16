package net.radstevee.packed.font

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import net.radstevee.packed.LOGGER
import net.radstevee.packed.font.FontProvider.BITMAP
import net.radstevee.packed.font.FontProvider.TRUETYPE
import net.radstevee.packed.fontAssetsNotFound
import net.radstevee.packed.key.Key
import net.radstevee.packed.pack.ResourcePack
import java.io.File
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.Path

/**
 * Represents a font.
 * @param key The name of the font.
 */
@Serializable
data class Font(@Transient var key: Key = Key("", "")) {
    /**
     * All font providers.
     */
    @SerialName("providers")
    val providersList: MutableList<FontProvider> = mutableListOf()

    /**
     * Adds a new font provider.
     * @param provider The font provider.
     * @see net.radstevee.packed.font.FontProvider
     */
    fun <P : FontProvider> addProvider(provider: P) {
        providersList.add(provider)
    }

    /**
     * Serializes the font down to JSON, ready to export to a font file.
     * @return the JSON
     */
    @OptIn(ExperimentalSerializationApi::class)
    fun json(): String {
        @Suppress("JSON_FORMAT_REDUNDANT") return Json {
            prettyPrint = true; explicitNulls = false; classDiscriminatorMode =
            ClassDiscriminatorMode.NONE; encodeDefaults = true
        }.encodeToString(this)
    }

    /**
     * Saves the resource pack.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun save(pack: ResourcePack) {
        key.createNamespace(pack)
        val unresolvedAssets = mutableListOf<Path>()
        providersList.forEach {
            when (it) {
                is BITMAP -> {
                    val exists = pack.assetResolutionStrategy.getTexture(it.key)?.exists() ?: false
                    if (!exists) unresolvedAssets.add(Path("assets/${it.key.namespace}/textures/${it.key.key}"))
                }

                is TRUETYPE -> {
                    val exists = pack.assetResolutionStrategy.getFont(it.key)?.exists() ?: false
                    if (!exists) unresolvedAssets.add(Path("assets/${it.key.namespace}/font/${it.key.key}"))
                }

                else -> {}
            }
        }
        // If there's any errors about unresolved assets, log them
        // We refuse to actually save this font and blame the pack author!
        if (unresolvedAssets.isNotEmpty()) {
            fontAssetsNotFound(unresolvedAssets, this)
            return
        }
        val file = File(pack.outputDir, "assets/${key.namespace}/font/${key.key}.json")
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeText(json())
        LOGGER.info("Font $key saved!")
    }

    /**
     * Builds a bitmap font provider and adds it.
     */
    inline fun bitmap(factory: BITMAP.() -> Unit) {
        addProvider(BITMAP().apply(factory))
    }

    /**
     * Builds a truetype font and adds it.
     */
    inline fun ttf(factory: TRUETYPE.() -> Unit) {
        addProvider(TRUETYPE().apply(factory))
    }

    companion object {
        /**
         * Builds a font and returns it.
         */
        inline fun font(factory: Font.() -> Unit): Font {
            return Font().apply(factory)
        }
    }
}