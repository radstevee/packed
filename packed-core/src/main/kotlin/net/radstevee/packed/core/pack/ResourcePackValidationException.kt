package net.radstevee.packed.core.pack

/**
 * An exception which happens when a [ResourcePackElement] failed to validate.
 * Note that this is only used inside of [Result]s and **never thrown**.
 * @param errorMessage The error message that will be displayed in the log.
 */
abstract class ResourcePackValidationException(
    val errorMessage: String,
    val warnMessage: String? = null,
) : Exception()
