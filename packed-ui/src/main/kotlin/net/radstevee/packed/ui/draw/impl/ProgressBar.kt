package net.radstevee.packed.ui.draw.impl

import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBar.bossBar
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

abstract class ProgressBar : Drawable {
    abstract val name: String
    abstract var width: Int
    abstract var colorOne: TextColor
    abstract var colorTwo: TextColor
    abstract override val renderPosition: RenderPosition
    abstract var percentageOne: Int
    abstract var percentageTwo: Int
    abstract var backgroundChar: Component
    abstract var barChar: Component

    init {
        lazy {
            if (percentageOne + percentageTwo != 100) error("Percentage $percentageOne+$percentageTwo does not equal to 100.")
        }
    }

    override fun initFonts(pack: ResourcePack) {
        pack.addFont {
            key = Key(PACKED_UI_NAMESPACE, "progress_bars/$name")
            bitmap {
                key = Key(PACKED_UI_NAMESPACE, "progress_bars/block.png")
                height = 127.0
                ascent = renderPosition.verticalShift
                chars = listOf("\uE000")
            }
        }
    }

    override fun draw(player: Player, component: Component) {
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

    override fun draw(player: Player) {
        draw(player, component())
    }

    fun component() = text().apply { builder ->
        builder.append(negativeSpace(renderPosition.horizontalShift))
        repeat(width) {
            builder.append(backgroundChar)
            builder.append(negativeSpace(-1))
        }
        builder.append(negativeSpace(-(width * 4)))
        val valueOne = (percentageOne * width) / 100
        val valueTwo = (percentageTwo * width) / 100
        repeat(valueOne) {
            builder.append(this.barChar.color(colorOne))
            builder.append(negativeSpace(-1))
        }
        repeat(valueTwo) {
            builder.append(this.barChar.color(colorTwo))
            builder.append(negativeSpace(-1))
        }
    }.build()

    override fun clear(player: Player) {
        BOSS_BARS[player.uniqueId]?.removeViewer(player)
        BOSS_BARS.remove(player.uniqueId)
    }

    private companion object {
        val BOSS_BARS = mutableMapOf<UUID, BossBar>()
    }
}