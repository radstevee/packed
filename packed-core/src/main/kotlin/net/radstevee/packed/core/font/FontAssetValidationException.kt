package net.radstevee.packed.core.font

import net.radstevee.packed.core.pack.ResourcePackValidationException
import java.nio.file.Path

/**
 * An exception when validating Font assets (Assets not found).
 */
public class FontAssetValidationException(
  /** The font. */
  public val font: Font,
  /** The assets that could not be resolved. */
  public val unresolvedAssets: List<Path>,
  /** The assets that were fallen back to. */
  public val fallbackAssets: List<Pair<Path, Path>>,
) : ResourcePackValidationException(
  if (unresolvedAssets.isNotEmpty()) {
    buildString {
      append("Following assets could not be resolved for font ${font.key}:\n")
      unresolvedAssets.forEach { path ->
        append("    - $path\n")
      }
      append("Verify that these assets actually exist with your asset resolution strategy.\n")
      append("Continuing. This font will not be saved!")
    }
  } else {
    null
  },
  if (fallbackAssets.isNotEmpty()) {
    buildString {
      append("Following assets could not be resolved for font ${font.key} but were fallen back to:\n")
      fallbackAssets.forEach { (original, fallback) ->
        append("    - $original -> $fallback")
      }
    }
  } else {
    null
  },
)
