package net.radstevee.packed.ui.example

import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.arguments.IntegerArgumentType.integer
import io.papermc.paper.command.brigadier.Commands.argument
import io.papermc.paper.command.brigadier.Commands.literal
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor.BLACK
import net.kyori.adventure.text.format.NamedTextColor.BLUE
import net.kyori.adventure.text.format.NamedTextColor.RED
import net.radstevee.packed.core.asset.impl.ResourceAssetResolutionStrategy
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.PackFormat
import net.radstevee.packed.core.pack.ResourcePackBuilder.Companion.resourcePack
import net.radstevee.packed.negativespaces.NegativeSpaces
import net.radstevee.packed.ui.PackedUI
import net.radstevee.packed.ui.PackedUI.PACKED_UI_NAMESPACE
import net.radstevee.packed.ui.draw.RenderPosition
import net.radstevee.packed.ui.draw.impl.ProgressBar
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Example : JavaPlugin() {
    override fun onEnable() {
        val spaces = NegativeSpaces(fontKey = Key("packed-ui", "space"))
        val progressBar = ProgressBar(
            "example",
            50,
            BLUE,
            RED,
            50,
            50,
            RenderPosition(RenderPosition.Location.BOSS_BAR, -4.0),
            text("\uE000").font(key("packed-example", "progress_bar_background")).color(BLACK)
        )
        val pack = resourcePack {
            assetResolutionStrategy = ResourceAssetResolutionStrategy
            meta {
                format = PackFormat.LATEST
                description = "packed-ui example"
                outputDir =
                    File("/home/radsteve/Minecraft/Fabulously Optimized(1)/minecraft/resourcepacks/packed-example/")
            }
            install(spaces)
            PackedUI.init(spaces)
            PackedUI.initDrawable(progressBar)
            install(PackedUI)
        }
        pack.addFont {
            key = Key("packed-example", "progress_bar_background")
            bitmap {
                key = Key(PACKED_UI_NAMESPACE, "progress_bars/block.png")
                height = 127.0
                ascent = 0.0
                chars = listOf("\uE000")
            }
        }
        pack.save(true)

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) {
            it.registrar().register(
                literal("packed-ui")
                    .then(
                        argument("percentageOne", integer(0, 100))
                            .then(argument("percentageTwo", integer(0, 100))
                                .executes { ctx ->
                                    if (ctx.source.sender !is Player) {
                                        ctx.source.sender.sendMessage("Only players can execute this command!")
                                        return@executes 0
                                    }
                                    val percentageOne = ctx.getArgument("percentageOne", Int::class.java)
                                    val percentageTwo = ctx.getArgument("percentageTwo", Int::class.java)

                                    progressBar.percentageOne = percentageOne
                                    progressBar.percentageTwo = percentageTwo
                                    progressBar.draw(ctx.source.sender as Player)
                                    SINGLE_SUCCESS
                                }).build()
                    ).build()
            )
        }
    }
}