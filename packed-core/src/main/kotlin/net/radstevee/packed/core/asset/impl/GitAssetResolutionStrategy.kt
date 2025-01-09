package net.radstevee.packed.core.asset.impl

import com.github.syari.kgit.KGit
import net.radstevee.packed.core.asset.AssetResolutionStrategy
import java.io.File
import java.nio.file.Path

/**
 * A resolution strategy for git repositories.
 * @param repo The KGit repo.
 */
public class GitAssetResolutionStrategy(
    public val repo: KGit,
) : AssetResolutionStrategy {
    /**
     * The repo worktree directory.
     */
    public var directory: File = repo.repository.workTree

    /**
     * Sets the resolution strategy to use a subdirectory of the repo.
     * @param relativePath The relative path from the [directory].
     */
    public fun subDirectory(relativePath: Path): GitAssetResolutionStrategy {
        directory = File(directory, relativePath.toString())
        return this
    }

    override fun getAsset(relativePath: Path): File = File(directory, relativePath.toString())

    override fun copyAssets(targetFile: File) {
        directory.copyRecursively(targetFile, true)
    }
}
