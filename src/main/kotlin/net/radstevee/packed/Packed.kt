package net.radstevee.packed

import net.radstevee.packed.font.Font
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

fun changeLogger(newLogger: Logger) {
    LOGGER = newLogger
}

internal var LOGGER = LoggerFactory.getLogger("packed")

internal fun fontAssetsNotFound(assets: List<Path>, font: Font) {
    LOGGER.error("Error whilst trying to process font ${font.key}: Some assets couldn't be found:")
    assets.forEach {
        LOGGER.error("    $it")
    }
    LOGGER.error("Verify that these assets actually exist with your asset resolution strategy.")
    LOGGER.error("Continuing. This font will not be saved!")
}