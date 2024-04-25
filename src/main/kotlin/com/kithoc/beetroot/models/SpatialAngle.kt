package com.kithoc.beetroot.models

enum class SpatialAngle(val angle: Float) {
    VALUE_MINUS_22_5(-22.5f),
    VALUE_0(0f),
    VALUE_22_5(22.5f),
    VALUE_45(45f);

    companion object {
        operator fun invoke(angle: Float): SpatialAngle? =
            entries.find { it.angle == angle }
    }
}