package net.radstevee.packed.ui.example

import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import io.papermc.paper.command.brigadier.Commands.argument
import io.papermc.paper.command.brigadier.Commands.literal
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor.BLACK
import net.kyori.adventure.text.format.NamedTextColor.BLUE
import net.kyori.adventure.text.format.NamedTextColor.RED
import net.kyori.adventure.text.format.TextColor
import net.radstevee.packed.core.asset.impl.ResourceAssetResolutionStrategy
import net.radstevee.packed.core.font.FontProvider
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.PackFormat
import net.radstevee.packed.core.pack.ResourcePackBuilder.Companion.resourcePack
import net.radstevee.packed.negativespaces.NegativeSpaces
import net.radstevee.packed.ui.PackedUI
import net.radstevee.packed.ui.PackedUI.PACKED_UI_NAMESPACE
import net.radstevee.packed.ui.PackedUI.init
import net.radstevee.packed.ui.PackedUI.initDrawable
import net.radstevee.packed.ui.SimpleWidgetBackground
import net.radstevee.packed.ui.draw.RenderPosition
import net.radstevee.packed.ui.draw.impl.ProgressBar
import net.radstevee.packed.ui.draw.impl.SimpleWidget
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Example : JavaPlugin() {
    val progressBar = object : ProgressBar() {
        override val name = "example"
        override var width = 30
        override var percentageOne = 50
        override var percentageTwo = 50
        override var backgroundChar = text("\uE000").font(key("packed-example", "progress_bar_background")).color(BLACK)
        override var barChar = text("\uE000").font(key(PACKED_UI_NAMESPACE, "progress_bars/$name"))
        override val renderPosition = RenderPosition(RenderPosition.Location.BOSS_BAR, -4.0)
        override var colorOne: TextColor = RED
        override var colorTwo: TextColor = BLUE
    }
    val pinchFactory: FontProvider.TRUETYPE.() -> Unit = {
        key = Key("packed-example", "pinch.ttf")
        shift = listOf(0.0, 2.0)
        size = 7.0
        oversample = 2.0
    }
    val pinchProvider = FontProvider.TRUETYPE().apply(pinchFactory)
    val widget = object : SimpleWidget() {
        override var component = text("SOME COOL EXAMPLE TEXT!").font(key("packed-example", "pinch"))
        override val background = SimpleWidgetBackground.apply { ascent = -11.75 }
        override val name = "example"
        override val renderPosition = RenderPosition(RenderPosition.Location.BOSS_BAR, 10.0, -2)
        override val ttfParentFont = pinchProvider
        override var width = 50
        override var textWidth: Int
            get() = (component as TextComponent).content().length * 5 // this is a monospace font with every capitalised glyph being 5x5
            set(value) {}
    }
    val spaces = NegativeSpaces(fontKey = Key("packed-ui", "space"))
    val pack = resourcePack {
        assetResolutionStrategy = ResourceAssetResolutionStrategy(this::class.java)
        meta {
            format = PackFormat.LATEST
            description = "packed-ui example"
            outputDir =
                File("/home/radsteve/Minecraft/Fabulously Optimized(1)/minecraft/resourcepacks/packed-example/")
        }
        install(spaces)
        init(spaces)
        initDrawable(progressBar)
        initDrawable(widget)
        install(PackedUI)
    }

    override fun onEnable() {
        pack.addFont {
            key = Key("packed-example", "progress_bar_background")
            bitmap {
                key = Key(PACKED_UI_NAMESPACE, "progress_bars/block.png")
                height = 127.0
                ascent = 0.0
                chars = listOf("\uE000")
            }
        }
        pack.addFont {
            key = Key("packed-example", "pinch")
            addProvider(pinchProvider)
        }
        pack.save(true)

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) {
            it.registrar().register(
                literal("packed-ui").then(
                    literal("progress_bar").then(
                        argument("percentageOne", integer(0, 100)).then(
                            argument(
                                "percentageTwo",
                                integer(0, 100)
                            ).executes { ctx ->
                                if (ctx.source.executor !is Player) {
                                    ctx.source.executor?.sendMessage("Only players can execute this command!")
                                    return@executes 0
                                }
                                val percentageOne = ctx.getArgument("percentageOne", Int::class.java)
                                val percentageTwo = ctx.getArgument("percentageTwo", Int::class.java)

                                progressBar.percentageOne = percentageOne
                                progressBar.percentageTwo = percentageTwo
                                progressBar.draw(ctx.source.executor as Player)
                                SINGLE_SUCCESS
                            }).build()
                    )
                ).then(
                    literal("widget").executes { ctx ->
                        if (ctx.source.executor !is Player) {
                            ctx.source.executor?.sendMessage("Only players can execute this command!")
                            return@executes 0
                        }
                        widget.draw(ctx.source.executor as Player)
                        SINGLE_SUCCESS
                    }
                ).then(
                    literal("clear").executes { ctx ->
                        if (ctx.source.executor !is Player) {
                            ctx.source.executor?.sendMessage("Only players can execute this command!")
                            return@executes 0
                        }
                        val player = ctx.source.executor as Player
                        progressBar.clear(player)
                        widget.clear(player)
                        SINGLE_SUCCESS
                    }
                )
                    .build()
            )
        }
    }
}