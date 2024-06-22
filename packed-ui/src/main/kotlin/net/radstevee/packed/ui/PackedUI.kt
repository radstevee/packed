package net.radstevee.packed.ui

import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component.text
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.plugin.PackedPlugin
import net.radstevee.packed.negativespaces.NegativeSpaces
import net.radstevee.packed.ui.draw.Drawable
import java.io.File

object PackedUI : PackedPlugin {
    private lateinit var negativeSpaces: NegativeSpaces
    private val negativeSpaceFont get() = key(negativeSpaces.fontKey.namespace, negativeSpaces.fontKey.key)
    internal fun negativeSpace(space: Int) = text().append(text(negativeSpaces.getChar(space))).font(negativeSpaceFont)

    private val drawables = mutableListOf<Drawable>()
    var PACKED_UI_NAMESPACE = "packed-ui"

    override fun beforeSave(pack: ResourcePack) {
        val stream = javaClass.getResourceAsStream("/textures/progress_bars/block.png")
            ?: error("No block character found. Check that you're shading everything properly.")
        val outputFile = File(pack.outputDir, "assets/$PACKED_UI_NAMESPACE/textures/progress_bars/block.png")
        outputFile.parentFile.mkdirs()
        stream.copyTo(outputFile.outputStream())
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