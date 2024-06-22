package net.radstevee.packed.ui

import net.kyori.adventure.key.Key.key
import net.kyori.adventure.text.Component.text
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.plugin.PackedPlugin
import net.radstevee.packed.negativespaces.NegativeSpaces
import net.radstevee.packed.ui.draw.Drawable
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.copyTo

object PackedUI : PackedPlugin {
    internal lateinit var negativeSpaces: NegativeSpaces
    internal val negativeSpaceFont get() = key(negativeSpaces.fontKey.namespace, negativeSpaces.fontKey.key)
    internal fun negativeSpace(space: Int) = text().append(text(negativeSpaces.getChar(space))).font(negativeSpaceFont)

    private val drawables = mutableListOf<Drawable>()
    var PACKED_UI_NAMESPACE = "packed-ui"

    override fun beforeSave(pack: ResourcePack) {
        val resourcePath = Paths.get(
            this::class.java.getResource("/textures/progress_bars/block.png")?.toURI()
                ?: error("Somehow no block character found.")
        )
        resourcePath.copyTo(Path("${pack.outputDir}/assets/$PACKED_UI_NAMESPACE/textures/progress_bars/block.png"))
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