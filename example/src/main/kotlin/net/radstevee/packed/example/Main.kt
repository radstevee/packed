package net.radstevee.packed.example

import net.radstevee.packed.core.asset.impl.ResourceAssetResolutionStrategy
import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.PackFormat
import net.radstevee.packed.core.pack.ResourcePackBuilder.Companion.resourcePack
import net.radstevee.packed.negativespaces.NegativeSpaces
import java.io.File

fun main() {
    val pack = resourcePack {
        meta {
            description = "Packed Example"
            format = PackFormat.LATEST
            outputDir = File("/tmp/packed-example")
        }

        assetResolutionStrategy = ResourceAssetResolutionStrategy(this::class.java)
        val spaces = NegativeSpaces(fontKey = Key("packed-example", "space"))
        install(spaces)
        // clones the repo to /tmp/packed-test/resourcepacks with credentials
        // and uses the subdirectory "global" as asset source
        /* assetResolutionStrategy = GitAssetResolutionStrategy(KGit.cloneRepository {
            setURI("https://github.com/IslandPractice/resourcepacks")

            val username = System.getenv("GH_USER")
            val token = System.getenv("GH_TOKEN")
            setCredentialsProvider(UsernamePasswordCredentialsProvider(username, token))

            val output = File("/tmp/packed-test/resourcepacks")
            output.deleteRecursively()
            setDirectory(output)
        }).subDirectory(Path("global")) */
    }

    pack.addFont { // will NOT be saved
        key = Key("packed", "invalid_example")
        bitmap {
            key = Key("packed", "font/invalid_bitmap.png") // logs an error!
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

    pack.addFont { // will be saved
        key = Key("packed", "example")
        bitmap {
            key = Key("packed", "font/bitmap.png")
            height = 8.0
            ascent = 7.0
            chars = listOf("\uE001")
        }
    }

    pack.save(true)
    pack.createZip(File(pack.outputDir, "pack.zip"))
}