package net.radstevee.packed.core.asset.impl

import net.radstevee.packed.core.asset.AssetResolutionStrategy
import java.io.File
import java.nio.file.Path

/**
 * Asset resolution strategy for a source directory.
 */
public class SourceDirectoryAssetResolutionStrategy(
  /** The source directory. */
  public val sourceDirectory: File,
) : AssetResolutionStrategy {
  override fun getAsset(relativePath: Path): File = File(sourceDirectory, relativePath.toString())

  override fun copyAssets(targetFile: File) {
    sourceDirectory.copyRecursively(targetFile, true)
  }
}
