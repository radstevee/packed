package net.radstevee.packed.core.util

import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.Path
import kotlin.io.path.copyTo
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists

internal object FileUtil {
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
                    val targetPath = outputDir.resolve(resourcePath.relativize(file).toString())
                    file.copyTo(targetPath, overwrite = true)
                    return FileVisitResult.CONTINUE
                }

                override fun preVisitDirectory(
                    dir: Path,
                    attrs: BasicFileAttributes,
                ): FileVisitResult {
                    val targetPath = outputDir.resolve(resourcePath.relativize(dir).toString())
                    if (targetPath.notExists()) {
                        targetPath.createDirectories()
                    }
                    return FileVisitResult.CONTINUE
                }
            },
        )
    }
}
