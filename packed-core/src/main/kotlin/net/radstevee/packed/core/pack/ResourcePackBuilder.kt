package net.radstevee.packed.core.pack

import net.radstevee.packed.core.asset.AssetResolutionStrategy
import net.radstevee.packed.core.hook.PackedHook
import java.io.File

public class ResourcePackBuilder {
    public inner class Meta {
        /**
         * Description of a resource pack. Comes up in the selection screen.
         */
        public var description: String? = null

        /**
         * Pack format/version.
         */
        public var format: PackFormat = PackFormat.LATEST

        /**
         * Output directory of the Resource pack. This is where it will be saved.
         */
        public var outputDir: File = File("")
    }

    /**
     * The Metadata for this resourcepack. Gets set by [meta]
     */
    public lateinit var meta: Meta

    /**
     * The list of hooks installed in the pack.
     */
    public val hooks: MutableList<PackedHook> = mutableListOf<PackedHook>()

    /**
     * Metadata builder for this resourcepack.
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
     * Initialises a resource pack from meta.
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
    public lateinit var assetResolutionStrategy: AssetResolutionStrategy

    public companion object {
        /**
         * Builds a resource pack.
         * @return the pack.
         */
        public inline fun resourcePack(block: ResourcePackBuilder.() -> Unit): ResourcePack = ResourcePackBuilder().apply(block).create()
    }
}
