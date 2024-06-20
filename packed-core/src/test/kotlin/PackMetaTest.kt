import net.radstevee.packed.core.pack.PackFormat
import net.radstevee.packed.core.pack.ResourcePackBuilder.Companion.resourcePack
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PackMetaTest {
    @Test
    fun packMetaTest() {
        val pack = resourcePack {
            meta {
                description = "Test"
                format = PackFormat.V1_20_2
                outputDir = File("/tmp/pack")
            }
        }
        pack.save()
        val expected = """
            {
                "pack": {
                    "pack_format": 18,
                    "description": "Test"
                }
            }
        """.trimIndent()
        println(File("/tmp/pack/pack.mcmeta").readText())
        assertEquals(expected, File("/tmp/pack/pack.mcmeta").readText().trimIndent())
    }
}
