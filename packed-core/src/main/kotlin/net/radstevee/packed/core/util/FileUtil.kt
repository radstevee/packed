package net.radstevee.packed.core.util

import java.net.JarURLConnection
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.jar.JarFile

object FileUtil {
    /**
     * Recursively copies resources directory to a target path.
     * @param clazz The class where the resources should be loaded from.
     * @param sourceDir The relative path within the `resources` directory.
     * @param targetDir Full path to the target location.
     */
    fun copyResourceDirectory(clazz: Class<*>, sourceDir: String, targetDir: String) {
        val resourceUrl = clazz.getResource(sourceDir) ?: error("Resource not found: $sourceDir")
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

    /**
     * Copies jar resources recursively to a destination.
     * @param destination The destination.
     * @param jarConnection The jar URL connection.
     *
     * Example:
     * ```kt
     * val assetsFolder = this::class.java.getResource("/assets")
     * val connection = assetsFolder.openConnection() as JarURLConnection
     * copyJarResourcesRecursively(Path("/tmp"), connection)
     * ```
     */
    fun copyJarResourcesRecursively(destination: Path, jarConnection: JarURLConnection) {
        val jarFile = jarConnection.jarFile
        val iterator = jarFile.entries().asIterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.name.startsWith(jarConnection.entryName)) {
                if (!entry.isDirectory) {
                    jarFile.getInputStream(entry).use {
                        Files.copy(it, Paths.get(destination.toString(), entry.name))
                    }
                } else {
                    Files.createDirectories(Paths.get(destination.toString(), entry.name))
                }
            }
        }
    }
}