package net.radstevee.packed.example

import net.radstevee.packed.asset.impl.ResourceAssetResolutionStrategy
import net.radstevee.packed.key.Key
import net.radstevee.packed.pack.PackFormat
import net.radstevee.packed.pack.ResourcePackBuilder.Companion.resourcePack
import java.io.File

fun main() {
    val pack = resourcePack {
        meta {
            description = "Packed Example"
            format = PackFormat.LATEST
            outputDir = File("/tmp/packed-example")
        }
        assetResolutionStrategy = ResourceAssetResolutionStrategy()
    }

    pack.addFont {
        key = Key("packed", "example")
        bitmap {
            key = Key("packed", "font/bitmap.png")
            height = 8
            ascent = 7
            chars = listOf("\uE000")
        }
    }

    pack.save(true)
}