package net.radstevee.packed.example

import net.radstevee.packed.core.asset.impl.ResourceAssetResolutionStrategy
import net.radstevee.packed.core.font.FontProvider
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.model.ItemModel
import net.radstevee.packed.core.pack.PackFormat
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackBuilder.Companion.resourcePack
import net.radstevee.packed.negativespaces.NegativeSpaces
import java.io.File

val customItems = mutableListOf<Pair<Int, ItemModel>>()

fun create2dItem(
    pack: ResourcePack,
    texture: Key,
) {
    val key = Key("packed", "item/${texture.key.split("/").last().removeSuffix(".png")}")
    val customModelData = (customItems.lastOrNull()?.first ?: 0) + 1

    customItems.add(
        customModelData to
            pack.addItem(key) {
                parent = "item/generated"
                layerTexture(0, texture)
            },
    )
}

fun register2dItems(pack: ResourcePack) {
    pack.addItem(Key("minecraft", "popped_chorus_fruit")) {
        parent = "item/generated"
        layerTexture(0, Key("minecraft", "item/popped_chorus_fruit"))

        customItems.forEach { (customModelData, model) ->
            override {
                this.model = model.key
                customModelData(customModelData)
            }
        }
    }
}

fun main() {
    val pack =
        resourcePack {
            meta {
                description = "Packed Example"
                format = PackFormat.LATEST
                outputDir = File("/tmp/packed-example")
            }

            assetResolutionStrategy = ResourceAssetResolutionStrategy(this::class.java)
            val spaces = NegativeSpaces(fontKey = Key("packed", "space"))
            install(spaces)
            // clones the repo to /tmp/packed-test/resourcepacks with credentials
            // and uses the subdirectory "global" as asset source
            /* assetResolutionStrategy = GitAssetResolutionStrategy(KGit.cloneRepository {
                setURI("https://github.com/me/my-packs")

                val username = System.getenv("GH_USER")
                val token = System.getenv("GH_TOKEN")
                setCredentialsProvider(UsernamePasswordCredentialsProvider(username, token))

                val output = File("/tmp/packed-test/resourcepacks")
                output.deleteRecursively()
                setDirectory(output)
            }).subDirectory(Path("global")) */
        }

    pack.addFont {
        fallback {
            if (it is FontProvider.BITMAP) {
                if (!it.key.key.contains("invalid")) return@fallback null
                return@fallback Key("packed", "font/fallback_bitmap.png")
            }

            null
        }

        key = Key("packed", "fallback_example")
        bitmap {
            key = Key("packed", "font/invalid_bitmap.png") // logs a warning and falls back to fallback_bitmap.png!
            height = 8.0
            ascent = 7.0
            chars = listOf("\uE000")
        }
        bitmap {
            key = Key("packed", "font/bitmap.png")
            height = 8.0
            ascent = 7.0
            chars = listOf("\uE001")
        }
    }

    pack.addFont {
        key = Key("packed", "example")
        bitmap {
            key = Key("packed", "font/bitmap.png")
            height = 8.0
            ascent = 7.0
            chars = listOf("\uE001")
        }
    }

    create2dItem(pack, Key("packed", "item/bitmap.png"))
    create2dItem(pack, Key("packed", "item/bitmap2.png"))

    register2dItems(pack)

    pack.save(true)
    pack.createZip(File(pack.outputDir, "pack.zip"))
}
