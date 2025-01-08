package net.radstevee.packed.core.font

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.radstevee.packed.core.JSON
import net.radstevee.packed.core.PACKED_LOGGER
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackElement
import java.io.File
import java.nio.file.Path
import kotlin.io.path.copyTo

/**
 * Represents a font.
 * @param key The name of the font.
 */
@Serializable
public class Font(
    @Transient
    public var key: Key = Key("", ""),
) : ResourcePackElement {
    /**
     * The asset fallback strategy for when an asset could not be found.
     */
    @Transient
    private var fallbackStrategy: (FontProvider) -> Key? = { null }

    /**
     * Sets the asset fallback strategy.
     * @param block The strategy.
     */
    public fun fallback(block: (FontProvider) -> Key?) {
        fallbackStrategy = block
    }

    /**
     * All font providers.
     */
    @SerialName("providers")
    public val providersList: MutableList<FontProvider> = mutableListOf()

    /**
     * Adds a new font provider.
     * @param provider The font provider.
     * @see net.radstevee.packed.core.font.FontProvider
     */
    public fun <P : FontProvider> addProvider(provider: P) {
        providersList.add(provider)
    }

    /**
     * Serializes the font down to JSON, ready to export to a font file.
     * @return the JSON
     */
    public fun json(): String = JSON.encodeToString(this)

    override fun validate(pack: ResourcePack): Result<Unit> {
        val unresolvedAssets = mutableListOf<Path>()
        val fallbackAssets = mutableListOf<Pair<Path, Path>>()

        providersList.forEach {
            when (it) {
                is FontProvider.Bitmap -> {
                    val assetExists = pack.assetResolutionStrategy.getTexture(it.key)?.exists() ?: false
                    val file = File(pack.outputDir, "assets/${it.key.namespace}/textures/${it.key.key}")
                    val exists = file.exists()
                    val unresolved = !assetExists && !exists
                    val fallback = fallbackStrategy(it)

                    if (unresolved && fallback != null) {
                        val fallbackPath =
                            File(pack.outputDir, "assets/${fallback.namespace}/textures/${fallback.key}").toPath()
                        fallbackPath.copyTo(file.toPath())
                        fallbackAssets.add(file.toPath() to fallbackPath)
                        return@forEach
                    }

                    if (unresolved) unresolvedAssets.add(file.toPath())
                }

                is FontProvider.Truetype -> {
                    val assetExists = pack.assetResolutionStrategy.getFont(it.key)?.exists() ?: false
                    val file = File(pack.outputDir, "assets/${it.key.namespace}/font/${it.key.key}")
                    val exists = file.exists()
                    val unresolved = !assetExists && !exists
                    val fallback = fallbackStrategy(it)

                    if (unresolved && fallback != null) {
                        val fallbackPath =
                            File(pack.outputDir, "assets/${fallback.namespace}/font/${fallback.key}").toPath()
                        fallbackPath.copyTo(file.toPath())
                        fallbackAssets.add(file.toPath() to fallbackPath)
                        return@forEach
                    }

                    if (unresolved) unresolvedAssets.add(file.toPath())
                }

                else -> {}
            }
        }
        // If there's any errors about unresolved assets, log them.
        // We refuse to actually save this font and blame the pack author!
        if (unresolvedAssets.isNotEmpty() || fallbackAssets.isNotEmpty()) {
            return Result.failure(FontAssetValidationException(this, unresolvedAssets, fallbackAssets))
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
    public inline fun bitmap(block: FontProvider.Bitmap.() -> Unit) {
        addProvider(FontProvider.Bitmap().apply(block))
    }

    /**
     * Builds a truetype font provider and adds it.
     */
    public inline fun ttf(block: FontProvider.Truetype.() -> Unit) {
        addProvider(FontProvider.Truetype().apply(block))
    }

    /**
     * Builds a reference font provider and adds it.
     */
    public inline fun reference(block: FontProvider.Reference.() -> Unit) {
        addProvider(FontProvider.Reference().apply(block))
    }

    /**
     * Builds a space font provider and adds it.
     */
    public inline fun space(block: FontProvider.Space.() -> Unit) {
        addProvider(FontProvider.Space().apply(block))
    }

    public companion object {
        /**
         * Builds a font and returns it.
         */
        public inline fun font(block: Font.() -> Unit): Font = Font().apply(block)
    }
}
