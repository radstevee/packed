package net.radstevee.packed.util

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object FileUtil {
    fun copyResourceDirectory(sourceDir: String, targetDir: String) {
        val loader = Thread.currentThread().contextClassLoader
        val resourceUrl = loader.getResource(sourceDir) ?: throw IllegalArgumentException("Resource not found: $sourceDir")
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