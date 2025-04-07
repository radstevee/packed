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
import org.jetbrains.annotations.UnmodifiableView
import org.zeroturnaround.zip.ZipUtil
import java.io.File

/**
 * A resource pack, containing several [ResourcePackElement]s.
 */
public class ResourcePack(
  /** The resource pack metadata. */
  public var meta: ResourcePackMeta,
  /** The output directory of the resource pack, it will be saved here. */
  public var outputDir: File,
  /** The strategy used to resolve assets. */
  public var assetResolutionStrategy: AssetResolutionStrategy,
  /** Mutable list of resource pack elements in this pack. */
  private val _elements: MutableList<ResourcePackElement> = mutableListOf(),
  /** Mutable list of hooks in this pack. */
  private val _hooks: MutableList<PackedHook> = mutableListOf(),
) {
  /**
   * The fonts in the pack.
   */
  public val elements: @UnmodifiableView List<ResourcePackElement> get() = _elements.toList()

  /**
   * The hooks in the pack.
   */
  public val hooks: @UnmodifiableView List<PackedHook> get() = _hooks.toList()

  /**
   * Adds a resource pack element to this pack.
   * @param element The element.
   * @return The added element.
   */
  public fun <T : ResourcePackElement> addElement(element: T): T {
    _elements.add(element)
    hooks.forEach { hook -> hook.onAddElement(this, element) }
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
   * Adds an item model to this resource pack.
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
   * Builds a sound list from the given namespace and adds it to the resource pack.
   * @param namespace The namespace. Can be null if you would like to set it in the builder.
   * @return The added sound list.
   */
  public fun addSounds(
    namespace: String? = null,
    block: SoundList.() -> Unit,
  ): SoundList = addSounds(SoundList(namespace ?: "").apply(block))

  /**
   * Adds a basic sound to this resource pack.
   * A basic sound will be saved to the first (or a new) sound list of the
   * same namespace of the sound.
   * @param soundEvent The sound.
   * @return The modified/added sound list.
   */
  public fun addBasicSound(soundEvent: SoundEvent): SoundList {
    val existingSoundLists = elements.filterIsInstance<SoundList>().filter { list -> list.namespace == soundEvent.key.namespace }
    val soundList = existingSoundLists.firstOrNull() ?: SoundList(soundEvent.key.namespace, mutableListOf(soundEvent))

    if (soundEvent !in soundList.soundEvents) {
      soundList.add(soundEvent)
    }

    if (soundList !in elements) {
      addSounds(soundList)
    }

    return soundList
  }

  /**
   * Builds and adds a basic sound to this resource pack.
   * @param key The sound key.
   * @return The added sound list.
   */
  public fun addBasicSound(
    key: Key,
    block: SoundEvent.() -> Unit,
  ): SoundList = addBasicSound(SoundEvent.sound(key, block))

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
