package net.radstevee.packed.core.font

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.encodeJson
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackElement
import net.radstevee.packed.core.packedLogger
import java.io.File
import java.nio.file.Path
import kotlin.io.path.copyTo
import kotlin.properties.Delegates

/**
 * Represents a font inside of a resource pack, consisting of multiple font providers.
 */
public class Font private constructor(
    private val _providers: MutableList<FontProvider>,
) : ResourcePackElement {
    /**
     * Creates a font with the given key.
     * @param key The key.
     */
    public constructor(key: Key) : this(mutableListOf()) {
        this.key = key
    }

    /**
     * Creates a font with the minecraft:default key.
     */
    public constructor() : this(Key.minecraft("default"))

    public var key: Key by Delegates.notNull()

    /**
     * The asset fallback provider for when an asset could not be found.
     */
    private var fallbackProvider: (FontProvider) -> Key? = { null }

    /**
     * Sets the asset fallback provider.
     * @param provider The provider.
     */
    public fun fallback(provider: (FontProvider) -> Key?) {
        fallbackProvider = provider
    }

    /**
     * All font providers.
     */
    public val providersList: List<FontProvider> = _providers.toList()

    /**
     * Adds a new font provider.
     * @param provider The font provider.
     * @see net.radstevee.packed.core.font.FontProvider
     */
    public fun <P : FontProvider> addProvider(provider: P) {
        _providers.add(provider)
    }

    /**
     * Serializes the font down to JSON, ready to export to a font file.
     * @return the JSON
     */
    public fun json(): String? = CODEC.encodeJson(this)

    override fun validate(pack: ResourcePack): Result<Unit> {
        val unresolvedAssets = mutableListOf<Path>()
        val fallbackAssets = mutableListOf<Pair<Path, Path>>()

        providersList.forEach { provider ->
            when (provider) {
                is FontProvider.Bitmap -> {
                    val assetExists = pack.assetResolutionStrategy.getTexture(provider.key)?.exists() ?: false
                    val file = File(pack.outputDir, "assets/${provider.key.namespace}/textures/${provider.key.value}")
                    val path = file.toPath()
                    val exists = file.exists()
                    val unresolved = !assetExists && !exists
                    val fallback = fallbackProvider(provider)

                    if (unresolved && fallback != null) {
                        val fallbackPath = File(pack.outputDir, "assets/${fallback.namespace}/textures/${fallback.value}").toPath()
                        fallbackPath.copyTo(path)
                        fallbackAssets.add(path to fallbackPath)

                        return@forEach
                    }

                    if (unresolved) {
                        unresolvedAssets.add(path)
                    }
                }

                is FontProvider.Truetype -> {
                    val assetExists = pack.assetResolutionStrategy.getFont(provider.key)?.exists() ?: false
                    val file = File(pack.outputDir, "assets/${provider.key.namespace}/font/${provider.key.value}")
                    val path = file.toPath()
                    val exists = file.exists()
                    val unresolved = !assetExists && !exists
                    val fallback = fallbackProvider(provider)

                    if (unresolved && fallback != null) {
                        val fallbackPath = File(pack.outputDir, "assets/${fallback.namespace}/font/${fallback.value}").toPath()
                        fallbackPath.copyTo(path)
                        fallbackAssets.add(path to fallbackPath)
                        return@forEach
                    }

                    if (unresolved) {
                        unresolvedAssets.add(path)
                    }
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
        val file = File(pack.outputDir, "assets/${key.namespace}/font/${key.value}.json")
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeText(json() ?: error("failed encoding font"))
        packedLogger.info("Font $key saved!")
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
        public val CODEC: Codec<Font> =
            RecordCodecBuilder.create { instance ->
                instance
                    .group(
                        FontProviders.PROVIDER_CODEC
                            .listOf()
                            .fieldOf("providers")
                            .forGetter(Font::_providers),
                    ).apply(instance, ::Font)
            }

        /**
         * Builds a font and returns it.
         */
        public inline fun font(block: Font.() -> Unit): Font = Font().apply(block)
    }
}
