package net.radstevee.packed

import net.radstevee.packed.pack.PackFormat
import net.radstevee.packed.pack.ResourcePack
import net.radstevee.packed.pack.ResourcePackMeta
import java.io.File

class ResourcePackBuilder {
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

    private fun create(): ResourcePack {
        return ResourcePack(ResourcePackMeta.init(format, description), outputDir)
    }

    companion object {
        /**
         * Builds a resource pack.
         * @return the pack.
         */
        fun buildResourcePack(block: ResourcePackBuilder.() -> Unit): ResourcePack {
            return ResourcePackBuilder().apply(block).create()
        }
    }
}