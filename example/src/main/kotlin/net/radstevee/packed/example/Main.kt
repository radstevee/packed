package net.radstevee.packed.example

import net.radstevee.packed.core.asset.impl.ResourceAssetResolutionStrategy
import net.radstevee.packed.core.font.FontProvider
import net.radstevee.packed.core.item.definition.BasicItem
import net.radstevee.packed.core.item.definition.ItemDefinition
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.PackFormat
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackBuilder.Companion.resourcePack
import net.radstevee.packed.core.sound.SoundEvent
import net.radstevee.packed.core.sound.SoundList
import net.radstevee.packed.negativespaces.NegativeSpaces
import java.io.File

public fun create2dItem(
    pack: ResourcePack,
    texture: Key,
) {
    val key = Key("packed", "item/${texture.value.split("/").last().removeSuffix(".png")}")

    pack.addItemModel(key) {
        parent = "item/generated"
        layerTexture(0, texture)
    }
    pack.addItemDefinition(ItemDefinition(key, BasicItem(key)))
}

public fun main() {
    val pack = resourcePack {
        meta {
            description = "Packed Example"
            format = PackFormat.LATEST
            outputDir = File("/tmp/packed-example")
        }

        assetResolutionStrategy = ResourceAssetResolutionStrategy(this::class.java)
        val spaces = NegativeSpaces(fontKey = Key("packed", "space"))
        install(spaces)
        // clones the repo to /tmp/packed-test/resourcepacks with credentials and uses the subdirectory "global" as asset source
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
        fallback { provider ->
            if (provider is FontProvider.Bitmap) {
                if (!provider.key.value.contains("invalid")) {
                    return@fallback null
                }
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

    pack.addGlobalTranslation("poop", "fart")

    pack.addSounds(
        SoundList(
            "packed_1",
            listOf(
                SoundEvent(
                    Key("packed_1", "some_sound"),
                    soundSet = listOf(
                        Key("packed_1", "some_sound"),
                        Key("packed_1", "some_other_sound")
                    )
                )
            )
        )
    )
    pack.addBasicSound(Key("packed", "some_sound"))

    pack.save(true)
    pack.createZip(File(pack.outputDir, "pack.zip"))
}
