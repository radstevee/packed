package net.radstevee.packed.core.hook

import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackElement

/**
 * A hook to get called for some resource pack save stages.
 */
public interface PackedHook {
  /**
   * Called before saving, but after copying assets to make sure there are no errors.
   */
  public fun beforeSave(pack: ResourcePack) {}

  /**
   * Called after saving. Keep in mind that there will be no automatic validation after it.
   */
  public fun afterSave(pack: ResourcePack) {}

  /**
   * Called after a resource pack element has been added into the pack
   */
  public fun onAddElement(
    pack: ResourcePack,
    element: ResourcePackElement,
  ) {}
}
