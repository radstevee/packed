import net.radstevee.packed.ResourcePackBuilder.Companion.buildResourcePack
import net.radstevee.packed.namespace.NamespacedKey
import net.radstevee.packed.font.Font
import net.radstevee.packed.font.FontProvider
import net.radstevee.packed.pack.PackFormat
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class FontTest {
    @Test
    fun fontTest() {
        val font = Font(NamespacedKey("islandpractice", "love_bug"))
        font.addProvider(
            FontProvider.TRUETYPE(
                NamespacedKey("islandpractice", "love_bug.ttf"),
                shift = listOf(0.0, 1.5),
                size = 8.0,
                oversample = 7.0
            )
        )
        font.addProvider(
            FontProvider.BITMAP(
                NamespacedKey("islandpractice", "foo"),
                height = 8,
                ascent = 7,
                chars = listOf("\u3433")
            )
        )

        val expected = """
            {
                "providers": [
                    {
                        "file": "islandpractice:love_bug.ttf",
                        "shift": [
                            0.0,
                            1.5
                        ],
                        "size": 8.0,
                        "oversample": 7.0
                    },
                    {
                        "file": "islandpractice:foo",
                        "height": 8,
                        "ascent": 7,
                        "chars": [
                            "㐳"
                        ]
                    }
                ]
            }
        """.trimIndent()
        println(font.json())
        assertEquals(expected, font.json().trimIndent())
    }

    @Test
    fun packWithFontTest() {
        val pack = buildResourcePack {
            description = "IslandPractice resources"
            format = PackFormat.LATEST
            outputDir = File("/tmp/pack")
        }
        pack.saveMeta()
        val font = Font(NamespacedKey("islandpractice", "love_bug"))
        font.addProvider(
            FontProvider.TRUETYPE(
                NamespacedKey("islandpractice", "love_bug.ttf"),
                shift = listOf(0.0, 1.5),
                size = 8.0,
                oversample = 7.0
            )
        )
        font.addProvider(
            FontProvider.BITMAP(
                NamespacedKey("islandpractice", "foo"),
                height = 8,
                ascent = 7,
                chars = listOf("\u3433")
            )
        )
        font.save(pack)
    }
}