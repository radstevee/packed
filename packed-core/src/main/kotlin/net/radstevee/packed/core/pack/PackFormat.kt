package net.radstevee.packed.core.pack

/**
 * Represents a pack format/version.
 */
public enum class PackFormat(
    /** The revision of the format. */
    public val rev: Int,
) {
    /** Minecraft 1.6.1 until 1.8.9 */
    V1_6_1_TO_1_8_9(1),

    /** Minecraft 1.9 until 1.10.2 */
    V1_9_TO_1_10_2(2),

    /** Minecraft 1.11 until 1.12.2 */
    V1_11_TO_1_12_2(3),

    /** Minecraft 1.13 until 1.14.4 */
    V1_13_TO_1_14_4(4),

    /** Minecraft 1.15 until 1.16.1 */
    V1_15_TO_1_16_1(5),

    /** Minecraft 1.16.2 until 1.16.5 */
    V1_16_2_TO_1_16_5(6),

    /** Minecraft 1.17 until 1.17.1 */
    V1_17_TO_1_17_1(7),

    /** Minecraft 1.18 until 1.18.2 */
    V1_18_TO_1_18_2(8),

    /** Minecraft 1.19 until 1.19.2 */
    V1_19_TO_1_19_2(9),

    /** Minecraft 1.19.3 */
    V1_19_3(12),

    /** Minecraft 1.19.4 */
    V1_19_4(13),

    /** Minecraft 1.20 until 1.20.1 */
    V1_20_TO_1_20_1(15),

    /** Minecraft 1.20.2 */
    V1_20_2(18),

    /** Minecraft 1.20.3 until 1.20.4 */
    V1_20_3_TO_1_20_4(22),

    /** Minecraft 1.20.5 until 1.20.6 */
    V1_20_5_TO_1_20_6(32),

    /** Minecraft 1.21 until 1.21.1 */
    V1_21_TO_1_21_1(34),

    /** Minecraft 1.21.2 */
    V1_21_2(42),

    /** Minecraft 1.21.4 */
    V1_21_4(46),

    /** The latest version at the time of updating packed. May not always be up-to-date. */
    LATEST(V1_21_4.rev),
}
