package net.radstevee.packed.core.util

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.radstevee.packed.core.codec.fixedSize

public data class Vec3i(
  val x: Int,
  val y: Int,
  val z: Int,
) {
  public companion object {
    public val CODEC: Codec<Vec3i> =
      Codec.INT.listOf().comapFlatMap(
        { ints ->
          fixedSize(ints, 3).map { list -> Vec3i(list[0], list[1], list[2]) }
        },
        { vec -> listOf(vec.x, vec.y, vec.z) },
      )
  }
}

public data class Vec3d(
  val x: Double,
  val y: Double,
  val z: Double,
) {
  public companion object {
    public val CODEC: Codec<Vec3d> =
      Codec.DOUBLE.listOf().comapFlatMap(
        { doubles ->
          fixedSize(doubles, 3).map { list -> Vec3d(list[0], list[1], list[2]) }
        },
        { vec -> listOf(vec.x, vec.y, vec.z) },
      )
  }
}

public data class Vec3f(
  public val x: Float,
  public val y: Float,
  public val z: Float,
) {
  public companion object {
    public val CODEC: Codec<Vec3f> =
      Codec.FLOAT.listOf().comapFlatMap(
        { floats ->
          fixedSize(floats, 3).map { floats -> Vec3f(floats[0], floats[1], floats[2]) }
        },
        { vec -> listOf(vec.x, vec.y, vec.z) },
      )
  }
}

public data class Mat2x2i(
  val x1: Int,
  val y1: Int,
  val x2: Int,
  val y2: Int,
) {
  public companion object {
    public val CODEC: Codec<Mat2x2i> = RecordCodecBuilder.create { instance ->
      instance
        .group(
          Codec.INT.fieldOf("x1").forGetter(Mat2x2i::x1),
          Codec.INT.fieldOf("y1").forGetter(Mat2x2i::y1),
          Codec.INT.fieldOf("x2").forGetter(Mat2x2i::x2),
          Codec.INT.fieldOf("y2").forGetter(Mat2x2i::y2),
        ).apply(instance, ::Mat2x2i)
    }
  }
}

public data class Mat2x2d(
  val x1: Double,
  val y1: Double,
  val x2: Double,
  val y2: Double,
) {
  public companion object {
    public val CODEC: Codec<Mat2x2d> = RecordCodecBuilder.create { instance ->
      instance
        .group(
          Codec.DOUBLE.fieldOf("x1").forGetter(Mat2x2d::x1),
          Codec.DOUBLE.fieldOf("y1").forGetter(Mat2x2d::y1),
          Codec.DOUBLE.fieldOf("x2").forGetter(Mat2x2d::x2),
          Codec.DOUBLE.fieldOf("y2").forGetter(Mat2x2d::y2),
        ).apply(instance, ::Mat2x2d)
    }
  }
}

/** Constructs a 3-dimensional integer vector. */
public fun vec(
  x: Int,
  y: Int,
  z: Int,
): Vec3i = Vec3i(x, y, z)

/** Constructs a 3-dimensional double vector. */
public fun vec(
  x: Double,
  y: Double,
  z: Double,
): Vec3d = Vec3d(x, y, z)

/** Constructs a 3-dimensional float vector. */
public fun vec(
  x: Float,
  y: Float,
  z: Float,
): Vec3f = Vec3f(x, y, z)

/** Constructs a 2x2-dimensional integer matrix. */
public fun mat(
  x1: Int,
  y1: Int,
  x2: Int,
  y2: Int,
): Mat2x2i = Mat2x2i(x1, y1, x2, y2)

/** Constructs a 2x2-dimensional double matrix. */
public fun mat(
  x1: Double,
  y1: Double,
  x2: Double,
  y2: Double
): Mat2x2d = Mat2x2d(x1, y1, x2, y2)
