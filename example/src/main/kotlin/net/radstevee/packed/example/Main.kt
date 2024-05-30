package net.radstevee.packed.example

import net.radstevee.packed.ResourcePackBuilder.Companion.resourcePack
import net.radstevee.packed.font.Font.Companion.font
import net.radstevee.packed.key.Key
import net.radstevee.packed.pack.PackFormat
import java.io.File

fun main() {
    val pack = resourcePack {
        meta {
            description = "Packed Example"
            format = PackFormat.LATEST
            outputDir = File("/tmp/packed-example")
        }
    }

    pack.fonts.add(font {
        key = Key("packed", "example")
        bitmap {
            key = Key("packed", "font/bitmap.png")
            height = 8
            ascent = 7
            chars = listOf("\uE000")
        }
    })

    pack.save(true)
}