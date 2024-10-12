# Working with Fonts & Font Providers

Vanilla has 5 font providers, which packed only supports 4 of:

## Bitmaps
The Bitmap provider is very straight-forward:
It takes in a Bitmap, a height, ascent and characters argument.

The client renders the bitmap, which must not be larger than 512x512, with the scale of the
`height` field and a vertical offset of the `ascent` field, which must not be larger than `height`.

It then maps the `chars` of this font to this bitmap.

See [the Minecraft wiki](https://minecraft.wiki/w/Font#Bitmap_provider).

::: info
Bitmap Providers are supported in packed:
```kt{4-9}
pack.addFont {
    key = ...
    
    bitmap {
        key = ...
        height = 8.0
        ascent = 7.0
        chars = listOf("\uE000")
    }
}
```
:::

## Spaces
The space provider defines a character's (or its glyph's) width.
It takes in a map of a character to its width like such:
```json
{
    "type": "space",
    "advances": {
        "\uE000": 20.0
    }
}
```

See [the Minecraft wiki](https://minecraft.wiki/w/Font#Space_provider).

::: info
Space providers are supported in packed:
```kt{4-9}
pack.addFont {
    key = ...
    
    space {
        advances = mutableMapOf(
            '\uE000' to 20.0,
            'A' to 50.0
        )
    }
}
```
:::

## TTFs
The TTF provider loads a compiled TrueType/OpenType font.

It takes in a resource location to the file, a `shift` array for horizontal and vertical offsets,
a `size` field, similar to a bitmap provider's `height`, an `oversample` field as the resolution
to render the font at, and a `skip` array of characters to ignore with this font.

:::info
TTF providers are supported in packed:
```kt{4-9}
pack.addFont {
    key = ...
    
    ttf {
        key = ...
        shift = listOf(5.0, 2.0)
        size = 10.0
        oversample = 9.0
    }
}
```
:::

See [the Minecraft wiki](https://minecraft.wiki/w/Font#TTF_provider).

## Unihex
The Unihex provider loads a zip file containing [GNU Unifont `.hex` files](https://en.wikipedia.org/wiki/GNU_Unifont#.hex_format).

::: info
Unihex providers are NOT supported in packed.
:::

## References
The reference provider includes another provider once. Can include other providers from fonts or resource packs.

Takes in an `id` field to the other font file.

::: info
Reference providers are supported in packed.
```kt{4-6}
pack.addFont {
    key = ...
    
    reference {
        provider = Key(...)
    }
}
```
:::