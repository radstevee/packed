package net.radstevee.packed.pack

import net.radstevee.packed.LOGGER
import net.radstevee.packed.asset.AssetResolutionStrategy
import net.radstevee.packed.font.Font
import java.io.File
import java.io.IOException

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
        mutableFonts.add(font)
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
        LOGGER.info("Building resource pack...")
        if (deleteOld) outputDir.deleteRecursively()
        outputDir.mkdirs()
        assetResolutionStrategy.copyAssets(outputDir)

        saveMeta()
        mutableFonts.forEach {
            it.save(this)
        }
        LOGGER.info("Resource pack saved!")
    }
}