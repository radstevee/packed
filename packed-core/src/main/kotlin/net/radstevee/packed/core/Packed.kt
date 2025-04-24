package net.radstevee.packed.core

import net.radstevee.packed.core.font.FontProviders
import net.radstevee.packed.core.item.definition.ItemDefinitionTypes
import net.radstevee.packed.core.item.definition.tint.Tints
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Changes the default packed logger. This is useful for when making a mod or plugin
 * and not wanting it to use the default logger, but rather your mods/plugins.
 * @param newLogger The new logger.
 */
public fun changePackedLogger(newLogger: Logger) {
  packedLogger = newLogger
}

/**
 * The packed logger.
 */
internal var packedLogger: Logger = LoggerFactory.getLogger("packed")

internal var bootstrapped = false

internal fun bootstrap() {
  if (bootstrapped) {
    return
  }

  Tints.bootstrap()
  ItemDefinitionTypes.bootstrap()
  FontProviders.bootstrap()

  bootstrapped = true
}
