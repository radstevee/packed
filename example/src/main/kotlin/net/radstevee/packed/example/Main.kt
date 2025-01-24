package net.radstevee.packed.example

import net.radstevee.packed.core.asset.impl.ResourceAssetResolutionStrategy
import net.radstevee.packed.core.font.FontProvider
import net.radstevee.packed.core.item.definition.BasicItem
import net.radstevee.packed.core.item.definition.ItemDefinition
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.lang.Language
import net.radstevee.packed.core.pack.PackFormat
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.pack.ResourcePackBuilder.Companion.resourcePack
import net.radstevee.packed.negativespaces.NegativeSpaces
import java.io.File

public fun create2dItem(
    pack: ResourcePack,
    texture: Key,
) {
    val key = Key("packed", "item/${texture.value.split("/").last().removeSuffix(".png")}")

    pack.addItemModel(key) {
        parent = "item/generated"
        primaryTexture(texture)
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

        assetResolutionStrategy = ResourceAssetResolutionStrategy(javaClass)
        val spaces = NegativeSpaces(fontKey = Key("packed", "space"))
        install(spaces)
        // clones the repo to a directory with credentials and uses the subdirectory "example" as asset source
        /* assetResolutionStrategy = GitAssetResolutionStrategy(KGit.cloneRepository {
            setURI("...")

            val username = System.getenv("GH_USER")
            val token = System.getenv("GH_TOKEN")
            setCredentialsProvider(UsernamePasswordCredentialsProvider(username, token))

            val output = File("...")
            output.deleteRecursively()
            setDirectory(output)
        }).subDirectory(Path("example")) */
    }

    pack.addFont {
        key = Key("packed", "fallback_example")

        fallback { fontProvider ->
            if (fontProvider is FontProvider.Bitmap) {
                if (!fontProvider.key.value.contains("invalid")) {
                    null
                }

                Key("packed", "font/fallback_bitmap.png")
            } else {
                null
            }
        }

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
    pack.addLanguage(
        Language(
            Key("packed", "the_packed_language"),
            buildMap {
                this["my.cool.translation.key"] = "cool"
            }
        )
    )
    pack.addTranslation(Key("packed", "the_packed_language"), "yes", "no")

    pack.addBasicSound(Key("packed", "my_sound"))
    pack.addSounds("packed_two") {
        namespace = "packed_two"

        add {
            key = Key("packed_two", "my_cool_sound_event")

            addSound(Key("packed_two", "some_sound"))
            addSound(Key("packed_1", "some_other_sound"))
        }
    }

    pack.save(true)
    pack.createZip(File(pack.outputDir, "pack.zip"))
}
