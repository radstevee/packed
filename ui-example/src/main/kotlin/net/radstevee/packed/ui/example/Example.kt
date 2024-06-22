package net.radstevee.packed.ui.example

import com.mojang.brigadier.Command
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.radstevee.packed.core.asset.impl.ResourceAssetResolutionStrategy
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.PackFormat
import net.radstevee.packed.core.pack.ResourcePackBuilder.Companion.resourcePack
import net.radstevee.packed.negativespaces.NegativeSpaces
import net.radstevee.packed.ui.PackedUI
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
            100,
            NamedTextColor.BLUE,
            NamedTextColor.RED,
            50,
            50,
            RenderPosition(RenderPosition.Location.BOSS_BAR, 20.0),
            text("")
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
        pack.save(true)

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) {
            it.registrar().register(
                Commands.literal("packed-ui").executes { ctx ->
                    if (ctx !is Player) return@executes 0

                    progressBar.draw(ctx)
                    Command.SINGLE_SUCCESS
                }.build()
            )
        }
    }
}