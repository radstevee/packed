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
) : ResourcePackValidationException(
        buildString {
            append("Error whilst trying to process font ${font.key}: Some assets couldn't be found:\n")
            unresolvedAssets.forEach {
                append("    - $it\n")
            }
            append("Verify that these assets actually exist with your asset resolution strategy.\n")
            append("Continuing. This font will not be saved!")
        },
    )
