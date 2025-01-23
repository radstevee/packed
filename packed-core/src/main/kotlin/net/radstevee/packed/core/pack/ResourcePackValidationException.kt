package net.radstevee.packed.core.pack

/**
 * An exception which happens when a [ResourcePackElement] failed to validate.
 * Note that this is only used inside of [Result]s and **never thrown**.
 */
public abstract class ResourcePackValidationException(
    /** An error message. */
    public val errorMessage: String? = null,
    /** A warning message. */
    public val warnMessage: String? = null,
) : Exception()
