package net.radstevee.packed.core.asset.impl

import net.radstevee.packed.core.asset.AssetResolutionStrategy
import net.radstevee.packed.core.util.FileUtil
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.outputStream

/**
 * Asset resolution strategy for resources.
 */
public class ResourceAssetResolutionStrategy(
  /** The class that should be used for resolving resources. */
  public val clazz: Class<*>,
) : AssetResolutionStrategy {
  override fun getAsset(relativePath: Path): File? {
    val stream = clazz.getResourceAsStream(relativePath.toString())
      ?: clazz.getResourceAsStream("/$relativePath")
      ?: return null
    val file = createTempFile()
    stream.let { stream ->
      file.outputStream().use { out ->
        stream.copyTo(out)
      }
    }

    return file.toFile()
  }
  override fun copyAssets(targetFile: File) {
    FileUtil.copyResourceDirectory(clazz, "assets", Path("${targetFile.path}/assets"))
  }
}
