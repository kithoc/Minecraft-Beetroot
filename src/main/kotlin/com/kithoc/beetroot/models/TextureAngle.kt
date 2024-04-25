package com.kithoc.beetroot.models

enum class TextureAngle(val angle: Int) {
    VALUE_0(0),
    VALUE_90(90),
    VALUE_180(180),
    VALUE_270(270);

    val next: TextureAngle
        get() = when (this) {
            VALUE_0 -> VALUE_90
            VALUE_90 -> VALUE_180
            VALUE_180 -> VALUE_270
            VALUE_270 -> VALUE_0
        }

    companion object {
        operator fun invoke(angle: Int): TextureAngle? =
            entries.find { angle == it.angle }
    }
}