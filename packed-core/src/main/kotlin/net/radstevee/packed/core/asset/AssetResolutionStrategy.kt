package net.radstevee.packed.core.asset

import net.radstevee.packed.core.key.Key
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path

/**
 * A strategy for resolving assets.
 */
interface AssetResolutionStrategy {
    /**
     * Resolves an asset.
     * @param relativePath The relative path to the root directory of the resources.
     * @return The asset, if found.
     */
    fun getAsset(relativePath: Path): File?

    /**
     * Gets a texture.
     * @param key The texture key.
     * @return The texture, if found.
     */
    fun getTexture(key: Key) = getAsset(Path("assets/${key.namespace}/textures/${key.key}"))

    /**
     * Gets a font.
     * @param key The font key.
     * @return The font, if found.
     */
    fun getFont(key: Key) =
        getAsset(Path("assets/${key.namespace}/font/${key.key}"))
            ?: getAsset(Path("assets/${key.namespace}/font/${key.key}.json"))

    /**
     * Copies asset files to a target directory.
     * @param targetFile The target directory.
     */
    fun copyAssets(targetFile: File)
}
