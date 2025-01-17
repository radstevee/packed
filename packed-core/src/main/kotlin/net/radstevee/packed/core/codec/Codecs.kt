package net.radstevee.packed.core.codec

import com.mojang.serialization.Codec
import net.radstevee.packed.core.util.Vec3f

public object Codecs {
    public val RGB_COLOR: Codec<Int> = Codec.withAlternative(Codec.INT, Vec3f.CODEC) { color ->
        colorFromFloat(1f, color.x, color.y, color.z)
    }
}
