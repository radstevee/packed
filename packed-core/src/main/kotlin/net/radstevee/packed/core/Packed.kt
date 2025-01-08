package net.radstevee.packed.core

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Changes the default packed logger. This is useful for when making a mod or plugin
 * and not wanting it to use the default logger, but rather your mods/plugins.
 * @param newLogger The new logger.
 */
public fun changeLogger(newLogger: Logger) {
    PACKED_LOGGER = newLogger
}

/**
 * The packed logger.
 */
internal var PACKED_LOGGER = LoggerFactory.getLogger("packed")
@OptIn(ExperimentalSerializationApi::class)
internal val JSON = Json {
    prettyPrint = true

    explicitNulls = false
    classDiscriminatorMode = ClassDiscriminatorMode.NONE
    encodeDefaults = true
}
