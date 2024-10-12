# Plugins
Packed is extensible by using Packed Plugins. These currently don't have access to more functionality
than you would with using the library regularly, but can register hooks before and after saving.

## Negative Spaces
Negative Spaces is a simple sample plugin which can create a negative space font in your pack.

To use it, simply call `ResourcePack#install`:
```kt{1,6}
val spaces = NegativeSpaces(fontKey = Key(...))

val pack = resourcePack {
    ...
    
    install(spaces)
}
```

## Making your own
To create your own packed plugin, simply implement the `PackedPlugin` interface and override the `beforeSave` and `afterSave`
functions. Then call `ResourcePack#install` with your plugin's instance, and those functions will be called before
and after saving the resource pack.

For example:
```kt
object MyEpicPlugin : PackedPlugin {
    override fun beforeSave(pack: ResourcePack) {
        println("Hello from before save!")
    }
    
    override fun afterSave(pack: ResourcePack) {
        println("Hello from after save! I can now do whatever I want 😈")
    }
}

val pack = resourcePack {
    ...
    
    install(MyEpicPlugin)
}
```