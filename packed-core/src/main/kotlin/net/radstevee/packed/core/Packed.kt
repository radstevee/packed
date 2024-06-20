package net.radstevee.packed.core

import net.radstevee.packed.core.font.Font
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

/**
 * Changes the default packed logger. This is useful for when making a mod or plugin
 * and not wanting it to use the default logger, but rather your mods/plugins.
 * @param newLogger The new logger.
 */
fun changeLogger(newLogger: Logger) {
    LOGGER = newLogger
}

/**
 * The packed logger.
 */
internal var LOGGER = LoggerFactory.getLogger("packed")

/**
 * Called when font assets couldn't be resolved using the given asset resolution strategy.
 * @param assets The assets which weren't found.
 * @param font The font in which the error occurred in.
 */
internal fun fontAssetsNotFound(assets: List<Path>, font: Font) {
    LOGGER.error("Error whilst trying to process font ${font.key}: Some assets couldn't be found:")
    assets.forEach {
        LOGGER.error("    - $it")
    }
    LOGGER.error("Verify that these assets actually exist with your asset resolution strategy.")
    LOGGER.error("Continuing. This font will not be saved!")
}