package net.radstevee.packed.ui

import net.radstevee.packed.core.key.Key
import net.radstevee.packed.ui.PackedUI.PACKED_UI_NAMESPACE

/**
 * The background for a simple widget. All of these textures should be 4x256 and somewhat transparent.
 * @param leftCorner The left edge of the background.
 * @param content The main part of the background.
 * @param rightCorner The right edge of the background.
 */
open class SimpleWidgetBackground(val leftCorner: Key, val content: Key, val rightCorner: Key, var ascent: Double = 0.0) {
    companion object : SimpleWidgetBackground(
        Key(PACKED_UI_NAMESPACE, "widget_backgrounds/left_corner.png"),
        Key(PACKED_UI_NAMESPACE, "widget_backgrounds/content.png"),
        Key(PACKED_UI_NAMESPACE, "widget_backgrounds/right_corner.png")
    )
}