package net.radstevee.packed.core.asset.impl

import com.github.syari.kgit.KGit
import net.radstevee.packed.core.asset.AssetResolutionStrategy
import java.io.File
import java.nio.file.Path

/**
 * A resolution strategy for git repositories.
 * @param repo The KGit repo.
 */
class GitAssetResolutionStrategy(repo: KGit) : AssetResolutionStrategy {
    /**
     * The repo worktree directory.
     */
    var directory: File = repo.repository.workTree

    /**
     * Sets the resolution strategy to use a subdirectory of the repo.
     * @param relativePath The relative path from the [directory].
     */
    fun subDirectory(relativePath: Path): GitAssetResolutionStrategy {
        directory = File(directory, relativePath.toString())
        return this
    }

    override fun getAsset(relativePath: Path) = File(directory, relativePath.toString())

    override fun copyAssets(targetFile: File) {
        directory.copyRecursively(targetFile, true)
    }
}