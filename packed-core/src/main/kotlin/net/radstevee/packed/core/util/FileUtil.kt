package net.radstevee.packed.core.util

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.Path

object FileUtil {
    /**
     * Recursively copies resources directory to a target path.
     * @param clazz The class where the resources should be loaded from.
     * @param sourceDir The relative path within the `resources` directory.
     * @param targetDir Full path to the target location.
     */
    fun copyResourceDirectory(
        clazz: Class<*>,
        sourceDir: String,
        targetDir: String,
    ) {
        val resourceUrl = clazz.getResource(sourceDir) ?: error("Resource not found: $sourceDir")
        val resourcePath = Paths.get(resourceUrl.toURI())
        val outputDir = Path(targetDir)

        Files.walkFileTree(
            resourcePath,
            object : SimpleFileVisitor<Path>() {
                override fun visitFile(
                    file: Path,
                    attrs: BasicFileAttributes,
                ): FileVisitResult {
                    // Determine the target file path
                    val targetPath = outputDir.resolve(resourcePath.relativize(file).toString())
                    // Copy the file to the target path
                    Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING)
                    return FileVisitResult.CONTINUE
                }

                override fun preVisitDirectory(
                    dir: Path,
                    attrs: BasicFileAttributes,
                ): FileVisitResult {
                    // Determine the target directory path
                    val targetPath = outputDir.resolve(resourcePath.relativize(dir).toString())
                    // Create the target directory if it doesn't exist
                    if (!Files.exists(targetPath)) {
                        Files.createDirectories(targetPath)
                    }
                    return FileVisitResult.CONTINUE
                }
            },
        )
    }
}
