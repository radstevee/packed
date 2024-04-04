package net.radstevee.packed.pack

import net.radstevee.packed.font.Font
import java.io.File
import java.io.IOException
import kotlin.jvm.Throws

/**
 * A resource pack.
 * @param meta The resource pack meta.
 * @param outputDir Output directory of the resource pack. This is where it will be saved.
 */
class ResourcePack(
    val meta: ResourcePackMeta,
    val outputDir: File,
    val fonts: MutableList<Font> = mutableListOf()
) {
    /**
     * Saves the `pack.mcmeta` file.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun saveMeta() {
        outputDir.mkdirs()
        val metaFile = File(outputDir, "pack.mcmeta")
        metaFile.parentFile.mkdirs()
        metaFile.createNewFile()
        metaFile.writeText(meta.json())
    }
}