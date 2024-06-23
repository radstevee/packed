package net.radstevee.packed.ui.draw

import net.kyori.adventure.text.Component
import net.radstevee.packed.core.pack.ResourcePack
import org.bukkit.entity.Player

/**
 * Represents something that can be drawn to a player.
 * This can be a chat message (soon™), action bar, boss bar or any other kind of overlay.
 */
interface Drawable {
    val renderPosition: RenderPosition
    fun initFonts(pack: ResourcePack) {}
    fun draw(player: Player)
    fun draw(player: Player, component: Component) {
        draw(player)
    }
    fun clear(player: Player)
}