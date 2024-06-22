package net.radstevee.packed.ui.draw

import net.radstevee.packed.core.pack.ResourcePack
import org.bukkit.entity.Player

interface Drawable {
    val renderPosition: RenderPosition
    fun initFonts(pack: ResourcePack) {}
    fun draw(player: Player)
}