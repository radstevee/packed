package net.radstevee.packed.util

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object FileUtil {
    /**
     * Recursively copies a resources directory to a target path.
     * @param sourceDir The relative path within the `resources` directory.
     * @param targetDir Full path to the target location.
     */
    fun copyResourceDirectory(sourceDir: String, targetDir: String) {
        val loader = Thread.currentThread().contextClassLoader
        val resourceUrl = loader.getResource(sourceDir) ?: error("Resource not found: $sourceDir")
        val resourcePath = Paths.get(resourceUrl.toURI())
        Files.walk(resourcePath).forEach { source ->
            val target = Paths.get(targetDir, source.toString().substring(resourcePath.toString().length))
            if (Files.isDirectory(source)) {
                if (!Files.exists(target)) Files.createDirectories(target)
            } else {
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }
}