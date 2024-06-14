package net.radstevee.packed.pack

import net.radstevee.packed.asset.AssetResolutionStrategy
import net.radstevee.packed.assetsNotFound
import net.radstevee.packed.font.Font
import net.radstevee.packed.font.FontProvider
import java.io.File
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.Path

/**
 * A resource pack.
 * @param meta The resource pack meta.
 * @param outputDir Output directory of the resource pack. This is where it will be saved.
 * @param mutableFonts Mutable list of fonts.
 */
data class ResourcePack(
    val meta: ResourcePackMeta,
    val outputDir: File,
    val assetResolutionStrategy: AssetResolutionStrategy,
    private val mutableFonts: MutableList<Font> = mutableListOf(),
) {
    /**
     * The fonts in the pack.
     */
    val fonts get() = mutableFonts.toList()

    /**
     * Adds a font to the pack.
     * @param font The font.
     */
    fun addFont(font: Font) {
        val unresolvedAssets = mutableListOf<Path>()
        font.providersList.forEach {
            when (it) {
                is FontProvider.BITMAP -> {
                    assetResolutionStrategy.getTexture(it.key)
                        ?: unresolvedAssets.add(Path("assets/${it.key.namespace}/textures/${it.key.key}"))
                }

                is FontProvider.TRUETYPE -> {
                    assetResolutionStrategy.getFont(it.key)
                        ?: unresolvedAssets.add(Path("assets/${it.key.namespace}/font/${it.key.key}"))
                }

                else -> {}
            }
        }
        // If there's any errors about unresolved assets, log them
        // but not actually quit, because it might still work. We blame the pack author!
        if (unresolvedAssets.isNotEmpty()) assetsNotFound(unresolvedAssets)
    }

    /**
     * Adds a font to the pack.
     * @param factory The font factory.
     */
    inline fun addFont(factory: Font.() -> Unit) {
        addFont(Font.font(factory))
    }

    private fun saveMeta() {
        val metaFile = File(outputDir, "pack.mcmeta")
        metaFile.parentFile.mkdirs()
        metaFile.createNewFile()
        metaFile.writeText(meta.json())
    }

    /**
     * Saves the entire pack. Should only be called after having added everything.
     * @throws IOException
     * @param deleteOld Whether it should delete all old files.
     */
    fun save(deleteOld: Boolean = false) {
        if (deleteOld) outputDir.deleteRecursively()
        outputDir.mkdirs()
        assetResolutionStrategy.copyAssets(outputDir)

        saveMeta()
        mutableFonts.forEach {
            it.save(this)
        }
    }
}