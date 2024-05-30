import net.radstevee.packed.ResourcePackBuilder.Companion.resourcePack
import net.radstevee.packed.font.Font.Companion.font
import net.radstevee.packed.key.Key
import net.radstevee.packed.pack.PackFormat
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class FontTest {
    @Test
    fun `Font Test - includes Bitmaps and TTFs`() {
        val font = font {
            key = Key("packed", "example")
            bitmap {
                key = Key("packed", "pog.png")
                height = 8
                ascent = 7
                chars = listOf("\uE000")
            }

            ttf {
                key = Key("packed", "comicsans.ttf")
                size = 16.0
                oversample = 9.0
                shift = listOf(0.0, -3.5)
            }
        }
        val expected = """
            {
                "providers": [
                    {
                        "file": "packed:pog.png",
                        "height": 8,
                        "ascent": 7,
                        "chars": [
                            ""
                        ],
                        "type": "bitmap"
                    },
                    {
                        "file": "packed:comicsans.ttf",
                        "shift": [
                            0.0,
                            -3.5
                        ],
                        "size": 16.0,
                        "oversample": 9.0,
                        "type": "ttf"
                    }
                ]
            }
        """.trimIndent()
        println(font.json())
        assertEquals(expected, font.json().trimIndent())
    }

    @Test
    fun `Font test, integrated into a basic resource pack`() {
        val pack = resourcePack {
            meta {
                description = "Packed test resources"
                format = PackFormat.V1_20_2
                outputDir = File("/tmp/pack")
            }
        }
        val expectedMeta = """
            {
                "pack": {
                    "pack_format": 18,
                    "description": "Packed test resources"
                }
            }
        """.trimIndent()
        val font = font {
            key = Key("packed", "example")
            bitmap {
                key = Key("packed", "pog.png")
                height = 8
                ascent = 7
                chars = listOf("\uE000")
            }

            ttf {
                key = Key("packed", "comicsans.ttf")
                size = 16.0
                oversample = 9.0
                shift = listOf(0.0, -3.5)
            }
        }
        pack.fonts.add(font)
        val expected = """
            {
                "providers": [
                    {
                        "file": "packed:pog.png",
                        "height": 8,
                        "ascent": 7,
                        "chars": [
                            ""
                        ],
                        "type": "bitmap"
                    },
                    {
                        "file": "packed:comicsans.ttf",
                        "shift": [
                            0.0,
                            -3.5
                        ],
                        "size": 16.0,
                        "oversample": 9.0,
                        "type": "ttf"
                    }
                ]
            }
        """.trimIndent()

        pack.save()
        assertEquals(expectedMeta, File("/tmp/pack/pack.mcmeta").readText().trimIndent())
        assertEquals(expected, File("/tmp/pack/assets/packed/font/example.json").readText().trimIndent())
    }
}
