package net.radstevee.packed.core.font

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.radstevee.packed.core.codec.IdMapper

public object FontProviders {
    private val ID_MAPPER: IdMapper<String, MapCodec<out FontProvider>> = IdMapper()
    public val PROVIDER_CODEC: Codec<FontProvider> = ID_MAPPER.codec(Codec.STRING).dispatch(FontProvider::providerCodec) { it }

    init {
        ID_MAPPER["bitmap"] = FontProvider.Bitmap.CODEC
        ID_MAPPER["space"] = FontProvider.Space.CODEC
        ID_MAPPER["ttf"] = FontProvider.Truetype.CODEC
        ID_MAPPER["reference"] = FontProvider.Reference.CODEC
    }
}
