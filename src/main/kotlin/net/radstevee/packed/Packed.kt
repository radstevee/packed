package net.radstevee.packed

import mu.KotlinLogging
import java.nio.file.Path

val LOGGER = KotlinLogging.logger("packed")

fun assetsNotFound(assets: List<Path>) {
    LOGGER.error("Some assets weren't found:")
    assets.forEach(::assetNotFound)
}

private fun assetNotFound(assetPath: Path) {
    LOGGER.error("    $assetPath")
}