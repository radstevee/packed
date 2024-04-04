import net.radstevee.packed.ResourcePackBuilder.Companion.buildResourcePack
import net.radstevee.packed.pack.PackFormat
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PackMetaTest {
    @Test
    fun packMetaTest() {
        val pack = buildResourcePack {
            description = "Test"
            format = PackFormat.V1_20_2
            outputDir = File("/tmp/pack")
        }
        pack.saveMeta()

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
