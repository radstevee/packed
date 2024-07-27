package net.radstevee.packed.core.pack

/**
 * Represents something inside of a resource pack which can be validated and saved.
 */
interface ResourcePackElement {
    /**
     * Validates this element and returns a [Result] of type Unit, with an optional exception
     * extending [ResourcePackValidationException]. This API will probably be improved in the future.
     * @param pack The resource pack.
     * @return A [Result].
     */
    fun validate(pack: ResourcePack): Result<Unit> = Result.success(Unit)

    /**
     * Saves this element to a resource pack.
     * @param pack The resource pack.
     */
    fun save(pack: ResourcePack)
}
