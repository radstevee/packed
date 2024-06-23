package net.radstevee.packed.ui.draw.impl

import com.google.gson.Gson
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.bossbar.BossBar.bossBar
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.radstevee.packed.core.font.FontProvider
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.ui.PackedUI.PACKED_UI_NAMESPACE
import net.radstevee.packed.ui.PackedUI.negativeSpace
import net.radstevee.packed.ui.SimpleWidgetBackground
import net.radstevee.packed.ui.draw.Drawable
import net.radstevee.packed.ui.draw.RenderPosition
import org.apache.commons.text.StringEscapeUtils
import org.bukkit.entity.Player
import java.util.UUID

abstract class SimpleWidget : Drawable {
    abstract val background: SimpleWidgetBackground
    abstract val name: String
    abstract val ttfParentFont: FontProvider.TRUETYPE
    abstract var width: Int
    abstract var component: Component
    abstract override val renderPosition: RenderPosition
    abstract var textWidth: Int

    override fun initFonts(pack: ResourcePack) {
        pack.addFont {
            key = Key(PACKED_UI_NAMESPACE, "simple_widgets/$name")
            ttf {
                key = ttfParentFont.key
                size = ttfParentFont.size
                oversample = ttfParentFont.oversample
                shift = ttfParentFont.shift.toMutableList().apply {
                    set(1, get(1) + renderPosition.verticalShift)
                }
            }

            listOf(background.leftCorner, background.content, background.rightCorner).forEachIndexed { i, it ->
                bitmap {
                    key = it
                    height = 256.0
                    ascent = background.ascent
                    chars = listOf(StringEscapeUtils.unescapeJava("\\uE00$i"))
                }
            }
        }
    }

    override fun draw(player: Player, component: Component) {
        when (renderPosition.location) {
            RenderPosition.Location.ACTION_BAR -> player.sendActionBar(component)
            RenderPosition.Location.BOSS_BAR -> {
                val existingBar = BOSS_BARS[player.uniqueId]
                if (existingBar != null) existingBar.name(component)
                else {
                    val bar = bossBar(component, 0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)
                    player.showBossBar(bar)
                    BOSS_BARS[player.uniqueId] = bar
                }
            }
        }
    }

    override fun draw(player: Player) = draw(player, component())

    private fun component() = text {
        it.append(negativeSpace(renderPosition.horizontalShift))
        val negativeOne = negativeSpace(-1)
        val widgetFont = key(PACKED_UI_NAMESPACE, "simple_widgets/$name")
        var currentWidth = width
        it.append(text("\uE000").font(widgetFont))
        currentWidth -= 4

        repeat(currentWidth - 4) { _ ->
            currentWidth--
            it.append(negativeOne)
            it.append(text("\uE001").font(widgetFont))
        }

        currentWidth -= 4
        it.append(negativeOne)
        it.append(text("\uE002").font(widgetFont))
        it.append(negativeSpace(-(textWidth + textWidth / 2 + renderPosition.horizontalShift)))
        it.append(component.font(widgetFont))
    }

    override fun clear(player: Player) {
        BOSS_BARS[player.uniqueId]?.removeViewer(player)
        BOSS_BARS.remove(player.uniqueId)
    }

    private companion object {
        val BOSS_BARS = mutableMapOf<UUID, BossBar>()
    }
}