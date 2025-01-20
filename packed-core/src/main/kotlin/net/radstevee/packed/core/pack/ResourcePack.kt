package net.radstevee.packed.core.pack

import net.radstevee.packed.core.asset.AssetResolutionStrategy
import net.radstevee.packed.core.font.Font
import net.radstevee.packed.core.hook.PackedHook
import net.radstevee.packed.core.item.ItemModel
import net.radstevee.packed.core.item.definition.ItemDefinition
import net.radstevee.packed.core.item.itemModel
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.lang.Language
import net.radstevee.packed.core.packedLogger
import net.radstevee.packed.core.sound.SoundEvent
import net.radstevee.packed.core.sound.SoundList
import org.zeroturnaround.zip.ZipUtil
import java.io.File

/**
 * A resource pack.
 * @param meta The resource pack meta.
 * @param outputDir Output directory of the resource pack. This is where it will be saved.
 * @param _elements Mutable list of elements in this resource pack.
 * @param _hooks Mutable list of packed hooks.
 */
public class ResourcePack(
    public val meta: ResourcePackMeta,
    public val outputDir: File,
    public val assetResolutionStrategy: AssetResolutionStrategy,
    private val _elements: MutableList<ResourcePackElement> = mutableListOf(),
    private val _hooks: MutableList<PackedHook> = mutableListOf(),
) {
    /**
     * The fonts in the pack.
     */
    public val elements: List<ResourcePackElement> get() = _elements.toList()

    /**
     * The hooks in the pack.
     */
    public val hooks: List<PackedHook> get() = _hooks.toList()

    /**
     * Adds a resource pack element to this pack.
     * @param element The element.
     * @return The added element.
     */
    public fun <T : ResourcePackElement> addElement(element: T): T {
        _elements.add(element)
        return element
    }

    /**
     * Adds a font to the pack.
     * @param font The font.
     * @return The added font.
     */
    public fun addFont(font: Font): Font = addElement(font)

    /**
     * Adds a font to the pack.
     * @param block The font block.
     * @return The added font.
     */
    public inline fun addFont(block: Font.() -> Unit): Font = addFont(Font.font(block))

    /**
     * Adds an item model to this resource pack.
     * @param model The item model.
     * @return The added model.
     */
    public fun addItemModel(model: ItemModel): ItemModel = addElement(model)

    /**
     * Adds an item model to this resouce pack.
     * @param key The model key.
     * @return The added model.
     */
    public inline fun addItemModel(
        key: Key,
        block: ItemModel.Builder.() -> Unit,
    ): ItemModel = addItemModel(itemModel(key, block))

    /**
     * Adds an item definition to this resource pack.
     * @param definition The definition.
     * @return The added definition.
     */
    public fun addItemDefinition(definition: ItemDefinition): ItemDefinition = addElement(definition)

    /**
     * Adds a language to this resource pack.
     * @param language The language.
     * @return The added language.
     */
    public fun addLanguage(language: Language): Language = addElement(language)

    /**
     * Adds a translation to a specified language of this resource pack.
     * @param languageKey The key of the wanted language.
     * @param key The translation key.
     * @param value The translation value.
     * @return The modified language.
     */
    public fun addTranslation(
        languageKey: Key,
        key: String,
        value: String,
    ): Language {
        val language =
            elements
                .filterIsInstance<Language>()
                .find { lang -> lang.key == languageKey }
                ?: Language(languageKey, mapOf(key to value)).also(::addLanguage)

        if (key !in language.translations) {
            language.translations = language.translations.plus(key to value)
        }

        return language
    }

    /**
     * Adds a translation to each Minecraft language.
     * @param key The translation key.
     * @param value The translation value.
     * @return The modified languages.
     */
    public fun addGlobalTranslation(
        key: String,
        value: String,
    ): List<Language> = Language.LANGUAGE_LIST.map { lang -> addTranslation(lang, key, value) }

    /**
     * Adds the given sound list to the resource pack.
     * @param soundList The sound list.
     * @return The added sound list.
     */
    public fun addSounds(soundList: SoundList): SoundList = addElement(soundList)

    /**
     * Adds a basic sound to this pack.
     * A basic sound will be saved to the first (or a new) sound list of the
     * same namespace of the sound.
     * @param soundEvent The sound.
     * @return The modified/added sound list.
     */
    public fun addBasicSound(soundEvent: SoundEvent): SoundList {
        val existingSoundLists = elements.filterIsInstance<SoundList>().filter { list -> list.namespace == soundEvent.key.namespace }
        val soundList = existingSoundLists.firstOrNull() ?: SoundList(soundEvent.key.namespace, listOf(soundEvent))

        if (soundEvent !in soundList.soundEvents) {
            soundList.soundEvents = soundList.soundEvents + soundEvent
        }

        if (soundList !in elements) {
            addSounds(soundList)
        }

        return soundList
    }

    /**
     * Adds a basic sound to this pack.
     * @param soundKey The key of the sound.
     * @return The added sound list.
     */
    public fun addBasicSound(soundKey: Key): SoundList = addBasicSound(SoundEvent(soundKey))

    /**
     * Saves the resource pack meta.
     */
    private fun saveMeta() {
        val metaFile = File(outputDir, "pack.mcmeta")
        metaFile.parentFile.mkdirs()
        metaFile.createNewFile()
        metaFile.writeText(meta.json() ?: error("failed encoding pack meta"))
    }

    /**
     * Saves the entire pack. Should only be called after having added everything.
     * @param deleteOld Whether it should delete all old files.
     */
    public fun save(deleteOld: Boolean = false) {
        packedLogger.info("Building resource pack...")
        if (deleteOld) {
            outputDir.deleteRecursively()
        }
        outputDir.mkdirs()
        assetResolutionStrategy.copyAssets(outputDir)
        _hooks.forEach { hook -> hook.beforeSave(this) }

        saveMeta()
        _elements.forEach { element ->
            val validationResult = element.validate(this)
            val exception = validationResult.exceptionOrNull() as ResourcePackValidationException?
            if (exception != null) {
                // Non-critical warnings
                if (exception.errorMessage == null && exception.warnMessage != null) {
                    exception.warnMessage.lines().forEach(packedLogger::warn)
                    element.save(this)

                    return@forEach
                }

                // Critical error message and potentially non-critical warnings
                if (exception.errorMessage != null) {
                    exception.errorMessage.lines().forEach(packedLogger::error)
                    exception.warnMessage?.lines()?.forEach(packedLogger::warn)
                }
            } else {
                element.save(this)
            }
        }

        _hooks.forEach { hook -> hook.afterSave(this) }
        packedLogger.info("Resource pack saved!")
    }

    /**
     * Creates a zip of the output directory.
     * @param outputFile The zip file.
     */
    public fun createZip(outputFile: File) {
        ZipUtil.pack(outputDir, outputFile)
        packedLogger.info("Pack successfully zipped to $outputFile!")
    }

    /**
     * Installs a hook.
     * @param hook The hook.
     */
    public fun install(hook: PackedHook) {
        _hooks.add(hook)
    }
}
