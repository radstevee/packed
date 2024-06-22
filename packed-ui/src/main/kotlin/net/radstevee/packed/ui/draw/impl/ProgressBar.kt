package net.radstevee.packed.ui.draw.impl

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBar.bossBar
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.TextColor
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.ui.PackedUI.PACKED_UI_NAMESPACE
import net.radstevee.packed.ui.PackedUI.negativeSpace
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
                height = 127.0
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
                    existingBar.name(component)
                } else {
                    val bar = bossBar(component, 0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)
                    player.showBossBar(bar)
                    BOSS_BARS[player.uniqueId] = bar
                }
            }
        }
    }

    val barCharacter = text().append(text("\uE000")).font(key(PACKED_UI_NAMESPACE, "progress_bars/$name")).build()
    val component
        get() = text().apply { builder ->
            repeat(width) {
                builder.append(backgroundChar)
                builder.append(negativeSpace(-1))
            }
            builder.append(negativeSpace(-(width * 4)))
            repeat((percentageOne * width) / 100) {
                builder.append(barCharacter.color(colorOne))
                builder.append(negativeSpace(-1))
            }
            repeat((percentageTwo * width) / 100) {
                builder.append(barCharacter.color(colorTwo))
                builder.append(negativeSpace(-1))
            }
        }.build()

    companion object {
        val BOSS_BARS = mutableMapOf<UUID, BossBar>()
    }
}