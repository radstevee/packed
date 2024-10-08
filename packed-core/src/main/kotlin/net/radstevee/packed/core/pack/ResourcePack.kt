package net.radstevee.packed.core.pack

import net.radstevee.packed.core.PACKED_LOGGER
import net.radstevee.packed.core.asset.AssetResolutionStrategy
import net.radstevee.packed.core.font.Font
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.model.ItemModel
import net.radstevee.packed.core.model.item
import net.radstevee.packed.core.plugin.PackedPlugin
import org.zeroturnaround.zip.ZipUtil
import java.io.File
import java.io.IOException

/**
 * A resource pack.
 * @param meta The resource pack meta.
 * @param outputDir Output directory of the resource pack. This is where it will be saved.
 * @param _elements Mutable list of elements in this resource pack.
 * @param _plugins Mutable list of packed plugins.
 */
data class ResourcePack(
    val meta: ResourcePackMeta,
    val outputDir: File,
    val assetResolutionStrategy: AssetResolutionStrategy,
    private val _elements: MutableList<ResourcePackElement> = mutableListOf(),
    private val _plugins: MutableList<PackedPlugin> = mutableListOf(),
) {
    /**
     * The fonts in the pack.
     */
    val elements get() = _elements.toList()

    /**
     * The plugins in the pack.
     */
    val plugins get() = _plugins.toList()

    /**
     * Adds a font to the pack.
     * @param font The font.
     * @return The added font.
     */
    fun addFont(font: Font): Font {
        _elements.add(font)
        return font
    }

    /**
     * Adds a font to the pack.
     * @param factory The font factory.
     * @return The added font.
     */
    inline fun addFont(factory: Font.() -> Unit): Font {
        val font = Font.font(factory)
        return addFont(font)
    }

    /**
     * Adds an item model to this resource pack.
     * @param model The item model.
     * @return The added model.
     */
    fun addItem(model: ItemModel): ItemModel {
        _elements.add(model)
        return model
    }

    /**
     * Adds an item model to this resouce pack.
     * @param key The model key.
     * @return The added model.
     */
    inline fun addItem(
        key: Key,
        block: ItemModel.Builder.() -> Unit,
    ): ItemModel {
        val model = item(key, block)
        return addItem(model)
    }

    /**
     * Saves the resource pack meta.
     */
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
        PACKED_LOGGER.info("Building resource pack...")
        if (deleteOld) outputDir.deleteRecursively()
        outputDir.mkdirs()
        assetResolutionStrategy.copyAssets(outputDir)
        _plugins.forEach { it.beforeSave(this) }

        saveMeta()
        _elements.forEach {
            val validationResult = it.validate(this)
            val exception = validationResult.exceptionOrNull() as ResourcePackValidationException?
            exception?.let { error ->
                if (error.errorMessage.isBlank() && error.warnMessage?.isNotBlank() != false) {
                    error.warnMessage?.lines()?.forEach(PACKED_LOGGER::warn)
                    it.save(this)

                    return@forEach
                }

                if (error.errorMessage.isNotBlank()) error.errorMessage.lines().forEach(PACKED_LOGGER::error)
                error.warnMessage?.lines()?.forEach(PACKED_LOGGER::warn)
            }

            if (exception == null) it.save(this)
        }

        _plugins.forEach { it.afterSave(this) }
        PACKED_LOGGER.info("Resource pack saved!")
    }

    /**
     * Creates a zip of the output directory.
     * @param outputFile The zip file.
     */
    fun createZip(outputFile: File) {
        ZipUtil.pack(outputDir, outputFile)
        PACKED_LOGGER.info("Pack successfully zipped to $outputFile!")
    }

    /**
     * Installs a plugin.
     * @param plugin The plugin.
     */
    fun install(plugin: PackedPlugin) {
        _plugins.add(plugin)
    }
}
