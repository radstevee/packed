package net.radstevee.packed.ui.draw.impl

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBar.bossBar
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.ui.PackedUI
import net.radstevee.packed.ui.PackedUI.PACKED_UI_NAMESPACE
import net.radstevee.packed.ui.draw.Drawable
import net.radstevee.packed.ui.draw.RenderPosition
import org.bukkit.entity.Player
import java.util.UUID

class ProgressBar(
    val name: String,
    val width: Int,
    var colorOne: TextColor,
    var colorTwo: TextColor,
    var percentageOne: Int,
    var percentageTwo: Int,
    override val renderPosition: RenderPosition,
    val backgroundChar: Component
) : Drawable {
    init {
        if (percentageOne + percentageTwo != 100) error("Percentage $percentageOne+$percentageTwo does not equal to 100.")
    }

    override fun initFonts(pack: ResourcePack) {
        pack.addFont {
            key = Key(PACKED_UI_NAMESPACE, "progress_bars/$name")
            bitmap {
                key = Key(PACKED_UI_NAMESPACE, "progress_bars/block.png")
                height = 256.0
                ascent = renderPosition.shift
                chars = listOf("\uE000")
            }
        }
    }

    override fun draw(player: Player) {
        when (renderPosition.location) {
            RenderPosition.Location.ACTION_BAR -> player.sendActionBar(component)
            RenderPosition.Location.BOSS_BAR -> {
                val existingBar = BOSS_BARS[player.uniqueId]
                if (existingBar != null) {
                    if (existingBar in player.activeBossBars()) existingBar.name(component)
                    else player.showBossBar(existingBar)
                } else {
                    val bar = bossBar(component, 0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)
                    player.showBossBar(bar)
                }
            }
        }
    }

    val barCharacter = text().append(text("\uE000")).font(key(PACKED_UI_NAMESPACE, name)).build()
    val component
        get() = text().apply { builder ->
            builder.append(PackedUI.negativeSpace(-width))
            repeat(width) {
                builder.append(backgroundChar)
            }
            repeat(percentageOne) {
                builder.append(barCharacter.color(colorOne))
            }
            repeat(percentageTwo) {
                builder.append(barCharacter.color(colorTwo))
            }
        }.build()

    companion object {
        val BOSS_BARS = mutableMapOf<UUID, BossBar>()
    }
}