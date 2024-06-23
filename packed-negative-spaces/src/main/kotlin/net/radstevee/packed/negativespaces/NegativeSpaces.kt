package net.radstevee.packed.negativespaces

import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.plugin.PackedPlugin

class NegativeSpaces(
    val fontKey: Key = Key("minecraft", "default"),
    val range: IntRange = -8192 .. 8192
) : PackedPlugin {
    val advances = buildMap {
        range.forEachIndexed { i, it ->
            put((START_UNICODE + i).toChar(), it.toDouble())
        }
    }.toMutableMap()

    override fun beforeSave(pack: ResourcePack) {
        pack.addFont {
            key = fontKey

            space {
                advances = this@NegativeSpaces.advances
            }
        }
    }

    fun getChar(space: Int) = advances.filterValues { it == space.toDouble() }.keys.first()

    companion object {
        var START_UNICODE = 0xCE000
    }
}