package net.radstevee.packed.asset.impl

import net.radstevee.packed.asset.AssetResolutionStrategy
import java.io.File
import java.nio.file.Path

/**
 * Asset resolution strategy for a source directory.
 * @param sourceDirectory The source directory.
 */
class SourceDirectoryAssetResolutionStrategy(val sourceDirectory: File) : AssetResolutionStrategy {
    override fun getAsset(relativePath: Path) = File(sourceDirectory, relativePath.toString())
    override fun copyAssets(targetFile: File) {
        sourceDirectory.copyRecursively(targetFile, true)
    }
}