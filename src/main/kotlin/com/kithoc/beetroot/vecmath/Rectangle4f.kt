package com.kithoc.beetroot.vecmath

@JvmRecord
data class Rectangle4f @Deprecated("use factories instead") constructor(
    val min: Vec2f, val max: Vec2f
) {
    companion object {
        fun fromTo(min: Vec2f, max: Vec2f) =
            Rectangle4f(min, max)

        fun fromTo(x1: Float, y1: Float, x2: Float, y2: Float) =
            fromTo(Vec2f(x1, y1), Vec2f(x2, y2))

        fun from(x: Range2f, y: Range2f) =
            fromTo(x.min, y.min, x.max, y.max)
    }
}