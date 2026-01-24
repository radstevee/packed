package net.radstevee.packed.core.pack

/**
 * Represents a pack format/version.
 */
public object PackFormat {
  /** Minecraft 1.6.1 until 1.8.9 */
  public const val V1_6_1_TO_1_8_9: Int = 1

  /** Minecraft 1.9 until 1.10.2 */
  public const val V1_9_TO_1_10_2: Int = 2

  /** Minecraft 1.11 until 1.12.2 */
  public const val V1_11_TO_1_12_2: Int = 3

  /** Minecraft 1.13 until 1.14.4 */
  public const val V1_13_TO_1_14_4: Int = 4

  /** Minecraft 1.15 until 1.16.1 */
  public const val V1_15_TO_1_16_1: Int = 5

  /** Minecraft 1.16.2 until 1.16.5 */
  public const val V1_16_2_TO_1_16_5: Int = 6

  /** Minecraft 1.17 until 1.17.1 */
  public const val V1_17_TO_1_17_1: Int = 7

  /** Minecraft 1.18 until 1.18.2 */
  public const val V1_18_TO_1_18_2: Int = 8

  /** Minecraft 1.19 until 1.19.2 */
  public const val V1_19_TO_1_19_2: Int = 9

  /** Minecraft 1.19.3 */
  public const val V1_19_3: Int = 12

  /** Minecraft 1.19.4 */
  public const val V1_19_4: Int = 13

  /** Minecraft 1.20 until 1.20.1 */
  public const val V1_20_TO_1_20_1: Int = 15

  /** Minecraft 1.20.2 */
  public const val V1_20_2: Int = 18

  /** Minecraft 1.20.3 until 1.20.4 */
  public const val V1_20_3_TO_1_20_4: Int = 22

  /** Minecraft 1.20.5 until 1.20.6 */
  public const val V1_20_5_TO_1_20_6: Int = 32

  /** Minecraft 1.21 until 1.21.1 */
  public const val V1_21_TO_1_21_1: Int = 34

  /** Minecraft 1.21.2 */
  public const val V1_21_2: Int = 42

  /** Minecraft 1.21.4 */
  public const val V1_21_4: Int = 46

  /** Minecraft 1.21.5 */
  public const val V1_21_5: Int = 55

  /** Minecraft 1.21.6 */
  public const val V1_21_6: Int = 63

  /** Minecraft 1.21.7 until 1.21.8 */
  public const val V1_21_7_TO_1_21_8: Int = 64

  /** Minecraft 1.21.9 until 1.21.10 */
  public const val V1_21_9_TO_1_21_10: Int = 69

  /** Minecraft 1.21.11 */
  public const val V1_21_11: Int = 75

  /** The latest version at the time of updating packed. May not always be up-to-date. */
  public const val LATEST: Int = V1_21_11
}
