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
public class ResourcePack(
    public val meta: ResourcePackMeta,
    public val outputDir: File,
    public val assetResolutionStrategy: AssetResolutionStrategy,
    private val _elements: MutableList<ResourcePackElement> = mutableListOf(),
    private val _plugins: MutableList<PackedPlugin> = mutableListOf(),
) {
    /**
     * The fonts in the pack.
     */
    public val elements: List<ResourcePackElement> get() = _elements.toList()

    /**
     * The plugins in the pack.
     */
    public val plugins: List<PackedPlugin> get() = _plugins.toList()

    /**
     * Adds a font to the pack.
     * @param font The font.
     * @return The added font.
     */
    public fun addFont(font: Font): Font {
        _elements.add(font)
        return font
    }

    /**
     * Adds a font to the pack.
     * @param factory The font factory.
     * @return The added font.
     */
    public inline fun addFont(factory: Font.() -> Unit): Font {
        val font = Font.font(factory)
        return addFont(font)
    }

    /**
     * Adds an item model to this resource pack.
     * @param model The item model.
     * @return The added model.
     */
    public fun addItem(model: ItemModel): ItemModel {
        _elements.add(model)
        return model
    }

    /**
     * Adds an item model to this resouce pack.
     * @param key The model key.
     * @return The added model.
     */
    public inline fun addItem(
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
     * @param deleteOld Whether it should delete all old files.
     */
    public fun save(deleteOld: Boolean = false) {
        PACKED_LOGGER.info("Building resource pack...")
        if (deleteOld) outputDir.deleteRecursively()
        outputDir.mkdirs()
        assetResolutionStrategy.copyAssets(outputDir)
        _plugins.forEach { it.beforeSave(this) }

        saveMeta()
        _elements.forEach { element ->
            val validationResult = element.validate(this)
            val exception = validationResult.exceptionOrNull() as ResourcePackValidationException?
            if (exception != null) {
                // Non-critical warnings
                if (exception.errorMessage == null && exception.warnMessage != null) {
                    exception.warnMessage.lines()?.forEach(PACKED_LOGGER::warn)
                    element.save(this)

                    return@forEach
                }

                // Critical error message and potentially non-critical warnings
                if (exception.errorMessage != null) {
                    exception.errorMessage.lines().forEach(PACKED_LOGGER::error)
                    exception.warnMessage?.lines()?.forEach(PACKED_LOGGER::warn)
                }
            } else {
                element.save(this)
            }
        }

        _plugins.forEach { plugin -> plugin.afterSave(this) }
        PACKED_LOGGER.info("Resource pack saved!")
    }

    /**
     * Creates a zip of the output directory.
     * @param outputFile The zip file.
     */
    public fun createZip(outputFile: File) {
        ZipUtil.pack(outputDir, outputFile)
        PACKED_LOGGER.info("Pack successfully zipped to $outputFile!")
    }

    /**
     * Installs a plugin.
     * @param plugin The plugin.
     */
    public fun install(plugin: PackedPlugin) {
        _plugins.add(plugin)
    }
}
