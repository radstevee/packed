package net.radstevee.packed.core.font

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
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path

/**
 * Represents a font.
 * @param key The name of the font.
 */
@Serializable
data class Font(
    @Transient var key: Key = Key("", ""),
) : ResourcePackElement {
    /**
     * All font providers.
     */
    @SerialName("providers")
    val providersList: MutableList<FontProvider> = mutableListOf()

    /**
     * Adds a new font provider.
     * @param provider The font provider.
     * @see net.radstevee.packed.core.font.FontProvider
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
        @Suppress("JSON_FORMAT_REDUNDANT")
        return Json {
            prettyPrint = true

            explicitNulls = false
            classDiscriminatorMode = ClassDiscriminatorMode.NONE
            encodeDefaults = true
        }.encodeToString(this)
    }

    override fun validate(pack: ResourcePack): Result<Unit> {
        val unresolvedAssets = mutableListOf<Path>()
        providersList.forEach {
            when (it) {
                is FontProvider.BITMAP -> {
                    val assetExists = pack.assetResolutionStrategy.getTexture(it.key)?.exists() ?: false
                    val exists = File(pack.outputDir, "assets/${it.key.namespace}/textures/${it.key.key}").exists()
                    if (!assetExists && !exists) unresolvedAssets.add(Path("assets/${it.key.namespace}/textures/${it.key.key}"))
                }

                is FontProvider.TRUETYPE -> {
                    val assetExists = pack.assetResolutionStrategy.getFont(it.key)?.exists() ?: false
                    val exists = File(pack.outputDir, "assets/${it.key.namespace}/font/${it.key.key}").exists()
                    if (!assetExists && !exists) unresolvedAssets.add(Path("assets/${it.key.namespace}/font/${it.key.key}"))
                }

                else -> {}
            }
        }
        // If there's any errors about unresolved assets, log them.
        // We refuse to actually save this font and blame the pack author!
        if (unresolvedAssets.isNotEmpty()) {
            return Result.failure(FontAssetValidationException(this, unresolvedAssets))
        }

        return Result.success(Unit)
    }

    override fun save(pack: ResourcePack) {
        key.createNamespace(pack)
        val file = File(pack.outputDir, "assets/${key.namespace}/font/${key.key}.json")
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeText(json())
        PACKED_LOGGER.info("Font $key saved!")
    }

    /**
     * Builds a bitmap font provider and adds it.
     */
    inline fun bitmap(factory: FontProvider.BITMAP.() -> Unit) {
        addProvider(FontProvider.BITMAP().apply(factory))
    }

    /**
     * Builds a truetype font provider and adds it.
     */
    inline fun ttf(factory: FontProvider.TRUETYPE.() -> Unit) {
        addProvider(FontProvider.TRUETYPE().apply(factory))
    }

    /**
     * Builds a reference font provider and adds it.
     */
    inline fun reference(factory: FontProvider.REFERENCE.() -> Unit) {
        addProvider(FontProvider.REFERENCE().apply(factory))
    }

    /**
     * Builds a space font provider and adds it.
     */
    inline fun space(factory: FontProvider.SPACE.() -> Unit) {
        addProvider(FontProvider.SPACE().apply(factory))
    }

    companion object {
        /**
         * Builds a font and returns it.
         */
        inline fun font(factory: Font.() -> Unit): Font = Font().apply(factory)
    }
}
