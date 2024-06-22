package net.radstevee.packed.negativespaces

import net.radstevee.packed.core.key.Key
import net.radstevee.packed.core.pack.ResourcePack
import net.radstevee.packed.core.plugin.PackedPlugin

class NegativeSpaces(
    val fontKey: Key = Key("minecraft", "default"),
    val range: IntRange = -8192 .. 8192
) : PackedPlugin {
    fun getAdvances() = buildMap {
        range.forEachIndexed { i, it ->
            put(START_UNICODE + i, it.toDouble())
        }
    }

    override fun beforeSave(pack: ResourcePack) {
        val spaceAdvances = getAdvances().mapKeys { it.key.toChar() }.toMutableMap()

        pack.addFont {
            key = fontKey

            space {
                advances = spaceAdvances
            }
        }
    }

    fun getChar(space: Int) = getAdvances().filterValues { it == space.toDouble() }.keys.first().toChar()

    companion object {
        var START_UNICODE = 0xD0000
    }
}