# Hooks

Packed hooks are functions that can run at specific resource pack events like before and after saving
and adding of elements.

## Negative Spaces

Negative Spaces is a simple example hook which can create a negative space font in your pack.

To use it, simply call `ResourcePack#install`:

```kt{1,6}
val spaces = NegativeSpaces(fontKey = Key(...))

val pack = resourcePack {
    ...
    
    install(spaces)
}
```

## Making your own

To create your own packed hook, simply implement the `PackedHook` interface and override the `beforeSave` and
`afterSave`
functions. Then call `ResourcePack#install` with your hook's instance, and those functions will be called before
and after saving the resource pack.

For example:

```kt
object MyEpicHook : PackedHook {
    override fun beforeSave(pack: ResourcePack) {
        println("Hello from before save!")
    }

    override fun afterSave(pack: ResourcePack) {
        println("Hello from after save! I can now do whatever I want 😈")
    }
}

val pack = resourcePack {
    ...

    install(MyEpicHook)
}
```
