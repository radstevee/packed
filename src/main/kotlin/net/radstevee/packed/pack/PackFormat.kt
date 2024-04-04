package net.radstevee.packed.pack

/**
 * Represents a pack format/version.
 */
@Suppress("unused")
enum class PackFormat(val rev: Int) {
    V1_6_1_TO_1_8_9(1),
    V1_9_TO_1_10_2(2),
    V1_11_TO_1_12_2(3),
    V1_13_TO_1_14_4(4),
    V1_15_TO_1_16_1(5),
    V1_16_2_TO_1_16_5(6),
    V1_17_TO_1_17_1(7),
    V1_18_TO_1_18_2(8),
    V1_19_TO_1_19_2(9),
    V1_19_3(12),
    V1_19_4(13),
    V1_20_TO_1_20_1(15),
    V1_20_2(18),
    V1_20_3_TO_1_20_4(22),

    LATEST(V1_20_3_TO_1_20_4.rev)
}