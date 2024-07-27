package net.radstevee.packed.core

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Changes the default packed logger. This is useful for when making a mod or plugin
 * and not wanting it to use the default logger, but rather your mods/plugins.
 * @param newLogger The new logger.
 */
fun changeLogger(newLogger: Logger) {
    PACKED_LOGGER = newLogger
}

/**
 * The packed logger.
 */
internal var PACKED_LOGGER = LoggerFactory.getLogger("packed")
