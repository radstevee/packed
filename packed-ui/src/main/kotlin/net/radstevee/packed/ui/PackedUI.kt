package net.radstevee.packed.ui

import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.empty
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.TextComponent
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.plugin.PackedPlugin
import net.radstevee.packed.negativespaces.NegativeSpaces
import net.radstevee.packed.ui.draw.Drawable
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object PackedUI : PackedPlugin {
    internal lateinit var negativeSpaces: NegativeSpaces
    private val negativeSpaceFont get() = key(negativeSpaces.fontKey.namespace, negativeSpaces.fontKey.key)
    internal fun negativeSpace(space: Int) = text(negativeSpaces.getChar(space)).font(negativeSpaceFont)

    private val drawables = mutableListOf<Drawable>()
    var PACKED_UI_NAMESPACE = "packed-ui"

    override fun beforeSave(pack: ResourcePack) {
        // TODO: improve
        listOf(
            "/textures/progress_bars/block.png",
            "/textures/widget_backgrounds/content.png",
            "/textures/widget_backgrounds/left_corner.png",
            "/textures/widget_backgrounds/right_corner.png"
        ).forEach {
            val stream = javaClass.getResourceAsStream(it)
                ?: error("No block character found. Check that you're shading everything properly.")
            val outputFile = File(pack.outputDir, "assets/$PACKED_UI_NAMESPACE/$it")
            outputFile.parentFile.mkdirs()
            stream.copyTo(outputFile.outputStream())
        }

        drawables.forEach {
            it.initFonts(pack)
        }
    }

    fun init(negativeSpaces: NegativeSpaces) {
        this.negativeSpaces = negativeSpaces
    }

    fun initDrawable(drawable: Drawable) {
        drawables.add(drawable)
    }
}