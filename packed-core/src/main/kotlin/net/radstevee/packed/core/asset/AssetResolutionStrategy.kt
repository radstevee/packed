package net.radstevee.packed.core.asset

import net.radstevee.packed.core.key.Key
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path

/**
 * A strategy for resolving assets.
 */
public interface AssetResolutionStrategy {
    /**
     * Resolves an asset.
     * @param relativePath The relative path to the root directory of the resources.
     * @return The asset, if found.
     */
    public fun getAsset(relativePath: Path): File?

    /**
     * Gets a texture.
     * @param key The texture key.
     * @return The texture, if found.
     */
    public fun getTexture(key: Key): File? = getAsset(Path("assets/${key.namespace}/textures/${key.value}"))

    /**
     * Gets a font.
     * @param key The font key.
     * @return The font, if found.
     */
    public fun getFont(key: Key): File? =
        getAsset(Path("assets/${key.namespace}/font/${key.value}"))
            ?: getAsset(Path("assets/${key.namespace}/font/${key.value}.json"))

    /**
     * Copies asset files to a target directory.
     * @param targetFile The target directory.
     */
    public fun copyAssets(targetFile: File)
}
