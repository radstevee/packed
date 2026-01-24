package net.radstevee.packed.core.pack

import net.radstevee.packed.core.asset.AssetResolutionStrategy
import net.radstevee.packed.core.hook.PackedHook
import java.io.File
import kotlin.properties.Delegates

/** A builder for resource packs. */
public class ResourcePackBuilder {
  /** A builder for resource pack meta. */
  public class Meta {
    /**
     * Description of a resource pack. Comes up in the selection screen.
     */
    public var description: String? = null

    /**
     * The format of this pack.
     */
    public var format: Int = PackFormat.LATEST

    /**
     * Output directory of the Resource pack. This is where it will be saved.
     */
    public var outputDir: File = File("")
  }

  /**
   * The Metadata for this resource pack.
   */
  public var meta: Meta by Delegates.notNull()

  /**
   * The list of hooks installed in the pack.
   */
  public val hooks: MutableList<PackedHook> = mutableListOf()

  /**
   * Metadata builder for this resource pack.
   * @param block The builder.
   */
  public inline fun meta(block: Meta.() -> Unit) {
    meta = Meta().apply(block)
  }

  /**
   * Installs a hook to the pack.
   */
  public fun install(hook: PackedHook) {
    hooks.add(hook)
  }

  /**
   * Creates this resource pack.
   */
  public fun create(): ResourcePack = ResourcePack(
    ResourcePackMeta.create(meta.format, meta.description),
    meta.outputDir,
    assetResolutionStrategy,
    _hooks = hooks,
  )

  /**
   * The strategy to resolve assets.
   */
  public var assetResolutionStrategy: AssetResolutionStrategy by Delegates.notNull()

  public companion object {
    /**
     * Builds a resource pack.
     * @return the pack.
     */
    public inline fun resourcePack(block: ResourcePackBuilder.() -> Unit): ResourcePack = ResourcePackBuilder().apply(block).create()
  }
}
