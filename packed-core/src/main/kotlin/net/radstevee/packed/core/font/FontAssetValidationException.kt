package net.radstevee.packed.core.font

import net.radstevee.packed.core.pack.ResourcePackValidationException
import java.nio.file.Path

/**
 * An exception when validating Font assets (Assets not found).
 * @param font The font.
 * @param unresolvedAssets The assets which couldn't be resolved.
 */
class FontAssetValidationException(
    val font: Font,
    val unresolvedAssets: List<Path>,
    val fallbackAssets: List<Pair<Path, Path>>,
) : ResourcePackValidationException(
        buildString {
            if (unresolvedAssets.isEmpty()) return@buildString
            append("Following assets could not be resolved for font ${font.key}:\n")
            unresolvedAssets.forEach {
                append("    - $it\n")
            }
            append("Verify that these assets actually exist with your asset resolution strategy.\n")
            append("Continuing. This font will not be saved!")
        },
        if (fallbackAssets.isNotEmpty()) {
            buildString {
                append("Following assets could not be resolved for font ${font.key} but were fallen back to:\n")
                fallbackAssets.forEach {
                    append("    - ${it.first} -> ${it.second}")
                }
            }
        } else {
            null
        },
    )
