package net.radstevee.packed.core.asset.impl

import net.radstevee.packed.core.asset.AssetResolutionStrategy
import net.radstevee.packed.core.util.FileUtil
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path

/**
 * Asset resolution strategy for resources.
 */
class ResourceAssetResolutionStrategy(
    val clazz: Class<*>,
) : AssetResolutionStrategy {
    override fun getAsset(relativePath: Path): File? = clazz.getResource(relativePath.toString())?.file?.let(::File)

    override fun copyAssets(targetFile: File) =
        FileUtil.copyResourceDirectory(clazz, "/assets", "${targetFile.path}${File.separator}assets")
}
