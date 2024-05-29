package net.radstevee.packed.pack

import net.radstevee.packed.font.Font
import net.radstevee.packed.util.FileUtil
import java.io.File
import java.io.IOException

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
    @Throws(IOException::class)
    private fun saveMeta() {
        val metaFile = File(outputDir, "pack.mcmeta")
        metaFile.parentFile.mkdirs()
        metaFile.createNewFile()
        metaFile.writeText(meta.json())
    }

    /**
     * Saves the entire pack. Should only be called after having added everything.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun save(deleteOld: Boolean = false) {
        if(deleteOld) outputDir.deleteRecursively()
        outputDir.mkdirs()
        FileUtil.copyResourceDirectory("assets", File(outputDir, "assets").path)
        saveMeta()
        fonts.forEach {
            it.save(this)
        }
    }
}