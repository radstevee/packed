import net.radstevee.packed.ResourcePackBuilder.Companion.buildResourcePack
import net.radstevee.packed.font.Font
import net.radstevee.packed.font.FontProvider
import net.radstevee.packed.namespace.NamespacedKey
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
                NamespacedKey("islandpractice", "foo.png"),
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
                        "oversample": 7.0,
                        "type": "ttf"
                    },
                    {
                        "file": "islandpractice:foo.png",
                        "height": 8,
                        "ascent": 7,
                        "chars": [
                            "㐳"
                        ],
                        "type": "bitmap"
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
            format = PackFormat.V1_20_2
            outputDir = File("/tmp/pack")
        }
        val expectedMeta = """
            {
                "pack": {
                    "pack_format": 18,
                    "description": "IslandPractice resources"
                }
            }
        """.trimIndent()
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
                NamespacedKey("islandpractice", "foo.png"),
                height = 8,
                ascent = 7,
                chars = listOf("\u3433")
            )
        )
        pack.fonts.add(font)
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
                        "oversample": 7.0,
                        "type": "ttf"
                    },
                    {
                        "file": "islandpractice:foo.png",
                        "height": 8,
                        "ascent": 7,
                        "chars": [
                            "㐳"
                        ],
                        "type": "bitmap"
                    }
                ]
            }
        """.trimIndent()

        pack.save()
        assertEquals(expectedMeta, File("/tmp/pack/pack.mcmeta").readText().trimIndent())
        assertEquals(expected, File("/tmp/pack/assets/islandpractice/font/love_bug.json").readText().trimIndent())
    }
}
