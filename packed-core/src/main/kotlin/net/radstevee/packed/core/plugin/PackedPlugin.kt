package net.radstevee.packed.core.plugin

import net.radstevee.packed.core.pack.ResourcePack

/**
 * A packed plugin.
 */
interface PackedPlugin {
    /**
     * Called before saving, but after copying assets to make sure there are no errors.
     */
    fun beforeSave(pack: ResourcePack) {}

    /**
     * Called after saving. Keep in mind that there will be no automatic validation after it.
     */
    fun afterSave(pack: ResourcePack) {}
}