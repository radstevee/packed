# packed
Packed is a library for creating on-the-fly resource packs / compiling resource packs using Kotlin.

## TODO

- [x] Copy asset files from resources
- [x] Dynamic generation of fonts
- [x] Dynamic generation of pack metas
- [ ] Model generation via code
- [ ] Shaders (?)
- [ ] Dynamic generation of sounds

**This is not production-ready!**

## Basic usage

Examples are located in the `examples` submodule.

```kotlin
val pack = resourcePack {
    meta {
        description = "Packed Example"
        format = PackFormat.LATEST
        outputDir = File("/tmp/packed-example")
    }
}
val font = font {
    key = Key("packed", "example")
    bitmap {
        key = Key("packed", "font/bitmap.png")
        height = 8
        ascent = 7
        chars = listOf("\uE000")
    }
}

pack.fonts.add(font)
pack.save(deleteOld = true)
```