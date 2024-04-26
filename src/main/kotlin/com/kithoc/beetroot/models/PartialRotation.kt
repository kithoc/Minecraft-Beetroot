package com.kithoc.beetroot.models

import com.kithoc.beetroot.vecmath.Axis3

enum class PartialRotation(val axis: Axis3, val angle: Float) {
    NONE(Axis3.X, 0f),
    X_MINUS_SIXTEENTH(Axis3.X, -22.5f),
    X_SIXTEENTH(Axis3.X, 22.5f),
    X_EIGHTH(Axis3.X, 45f),
    Y_MINUS_SIXTEENTH(Axis3.X, -22.5f),
    Y_SIXTEENTH(Axis3.X, 22.5f),
    Y_EIGHTH(Axis3.X, 45f),
    Z_MINUS_SIXTEENTH(Axis3.X, -22.5f),
    Z_SIXTEENTH(Axis3.X, 22.5f),
    Z_EIGHTH(Axis3.X, 45f);

    companion object {
        operator fun invoke(axis: Axis3, angle: Float): PartialRotation? =
            if (angle == 0f) NONE
            else entries.find { it.axis == axis && it.angle == angle }
    }
}