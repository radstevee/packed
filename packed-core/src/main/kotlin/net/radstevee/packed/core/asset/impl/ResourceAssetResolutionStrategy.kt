package net.radstevee.packed.core.asset.impl

import net.radstevee.packed.core.asset.AssetResolutionStrategy
import net.radstevee.packed.core.util.FileUtil
import java.io.File
import java.nio.file.Path

/**
 * Asset resolution strategy for resources.
 */
object ResourceAssetResolutionStrategy : AssetResolutionStrategy {
    override fun getAsset(relativePath: Path): File? {
        return Thread.currentThread().contextClassLoader.getResource(relativePath.toString())?.file?.let { File(it) }
    }

    override fun copyAssets(targetFile: File) {
        FileUtil.copyResourceDirectory("assets", "${targetFile.path}${File.separator}assets")
    }
}