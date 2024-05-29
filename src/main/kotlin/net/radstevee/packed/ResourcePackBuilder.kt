package net.radstevee.packed

import net.radstevee.packed.pack.PackFormat
import net.radstevee.packed.pack.ResourcePack
import net.radstevee.packed.pack.ResourcePackMeta
import java.io.File

class ResourcePackBuilder {
    inner class Meta {
        /**
         * Description of a resource pack. Comes up in the selection screen.
         */
        var description: String = ""

        /**
         * Pack format/version.
         */
        var format: PackFormat = PackFormat.LATEST

        /**
         * Output directory of the Resource pack. This is where it will be saved.
         */
        var outputDir: File = File("")
    }

    /**
     * The Metadata for this resourcepack. Gets set by [meta]
     */
    lateinit var meta: Meta

    /**
     * Metadata builder for this resourcepack.
     * @param factory The builder.
     */
    inline fun meta(factory: Meta.() -> Unit) {
        meta = Meta().apply(factory)
    }


    fun create(): ResourcePack {
        return ResourcePack(ResourcePackMeta.init(meta.format, meta.description), meta.outputDir)
    }

    companion object {
        /**
         * Builds a resource pack.
         * @return the pack.
         */
        inline fun resourcePack(block: ResourcePackBuilder.() -> Unit): ResourcePack {
            return ResourcePackBuilder().apply(block).create()
        }
    }
}