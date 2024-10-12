# Getting started

## Setup
::: code-group

```kts [build.gradle.kts]
repositories {
    maven("https://maven.radsteve.net")
}

dependencies {
    implementation("net.radstevee.packed:packed-core:VERSION")

    // If you would like to get the negative spaces plugin:
    implementation("net.radstevee.packed:packed-negative-spaces:VERSION")
}
```

```groovy [build.gradle]
repositories {
    maven {
        url "https://maven.radsteve.net"
    }
}

dependencies {
    implementation 'net.radstevee.packed:packed-core:VERSION'

    // If you would like to get the negative spaces plugin:
    implementation 'net.radstevee.packed:packed-negative-spaces:VERSION'
}
```

```xml [pom.xml]
<repositories>
    <repository>
        <id>rad-public</id>
        <url>https://maven.radsteve.net</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>net.radstevee.packed</groupId>
        <artifactId>packed-core</groupId>
        <version>VERSION</version>
    </dependency>
    <!-- If you would like to use the negative spaces plugin: -->
    <dependency>
        <groupId>net.radstevee.packed</groupId>
        <artifactId>packed-negative-spaces</groupId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

:::

Replace `VERSION` with your desired version of packed. You can view the latest version [here](https://github.com/radstevee/packed/releases/latest).

## Creating a resource pack

You can create a resource pack for Minecraft 1.20.4 with the `resourcePack` builder DSL like this:
```kt
val negativeSpaces = NegativeSpaces(fontKey = Key("packed", "negative-spaces"))

val pack = resourcePack {
    meta {
        description = "Awesome Packed resource pack!" // Pack description which will appear in the resource pack screen
        format = PackFormat.V1_20_4 // Pack format which adds compatibility warnings to the resource pack screen
        outputDir = File("...") // Where the pack will be generated
    }

    assetResolutionStrategy = ResourceAssetResolutionStrategy(this::class.java) // Will resolve assets from the resources of this JAR file

    // If you would like to use the negative spaces plugin:
    install(negativeSpaces)
}
```

## Adding a font

You can add fonts using the `ResourcePack#addFont` DSL like this:
```kt
// Generates a font to `assets/packed/font/my_awesome_font.json`
pack.addFont {
    key = Key("packed", "my_awesome_font")

    // Adds a bitmap provider to this font
    bitmap {
        key = Key("packed", "sprites/my_awesome_bitmap") // Uses `assets/packed/textures/sprites/my_awesome_bitmap.png`
        height = 8.0
        ascent = 7.0
        chars = listOf("\uE000")
    }
}
```

The resulting output of this would be:
```json
// assets/packed/font/my_awesome_font.json
{
  "providers": [
    {
      "type": "bitmap",
      "file": "packed:sprites/my_awesome_bitmap.png",
      "height": 8.0,
      "ascent": 7.0,
      "chars": [
        "\uE000"
      ]
    }
  ]
}
```

## Saving a resource pack

You can use the `ResourcePack#save` function to save a resource pack to the output directory.
This generates copies all assets and then saves all the files to the selected output directory.

```kt
pack.save(deleteOld = true) // Removes the directory and recreates it
```

:::info
If you would like to get a zip file, you can use the `ResourcePack#createZip(File)` function.
:::
