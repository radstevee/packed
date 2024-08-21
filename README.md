# packed
Packed is a library for compiling minecraft resource packs using Kotlin.

## TODO

- [x] Copy asset files from resources
- [x] Generation of fonts
- [x] Generation of pack metas
- [x] Different asset resolution strategies
- [x] Generation of (Item) Models
- [x] Fallback assets
- [ ] Generation of (Block) Models
- [ ] Generation of sounds
- [ ] Generation of Translatables/Languages
- [ ] Generation of Block states
- [ ] Generation of Core shader JSON files
- [ ] Generation of Colormaps
- [ ] Generation of animated textures
- [ ] Some Basic Packsquash Features (Compression, Minifying, Zip headers)
- [ ] Obfuscation

**All Pull requests and Issues are welcome!**

Packed is extensible by using/creating Packed plugins, which can hook into resource pack saving (and after).
For an example, check out [negative spaces](https://github.com/radstevee/packed/blob/master/packed-negative-spaces/).

## Examples

Examples are located in the `examples` submodule.
