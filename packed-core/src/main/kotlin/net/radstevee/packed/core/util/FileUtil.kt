package net.radstevee.packed.core.util

import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.PathWalkOption
import kotlin.io.path.createDirectories
import kotlin.io.path.createParentDirectories
import kotlin.io.path.inputStream
import kotlin.io.path.isDirectory
import kotlin.io.path.outputStream
import kotlin.io.path.walk

internal object FileUtil {
  /**
   * Recursively copies resources directory to a target path.
   * @param clazz The class where the resources should be loaded from.
   * @param sourceDir The relative path within the `resources` directory.
   * @param targetDir Full path to the target location.
   */
  @OptIn(ExperimentalPathApi::class)
  fun copyResourceDirectory(
    clazz: Class<*>,
    sourceDir: String,
    targetDir: Path,
  ) {
    val jar = Paths.get(clazz.protectionDomain.codeSource.location.toURI())

    targetDir.createParentDirectories()
    FileSystems.newFileSystem(jar).use { fs ->
      val rootPath = fs.getPath(sourceDir)
      rootPath.walk(PathWalkOption.INCLUDE_DIRECTORIES).forEach { path ->
        walkRecursively(path, rootPath, targetDir)
      }
    }
  }

  private fun walkRecursively(path: Path, rootPath: Path, targetDir: Path) {
    val relativePath = rootPath.relativize(path)
    val target = targetDir.resolve(relativePath.toString())

    if (path.isDirectory()) {
      target.createDirectories()
      return
    }

    path.inputStream().use { input ->
      target.createParentDirectories()
      target.outputStream().use { output ->
        input.copyTo(output)
      }
    }
  }
}
