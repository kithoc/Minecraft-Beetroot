package com.kithoc.beetroot.vecmath

@JvmRecord
data class Rectangle4i @Deprecated("use factories instead") constructor(
    val min: Vec2i, val max: Vec2i
) {
    val area: Int get() = (max.x - min.x) * (max.y - min.y)

    companion object {
        val ZERO = fromTo(Vec2i.ZERO, Vec2i.ZERO)

        fun fromTo(from: Vec2i, to: Vec2i) =
            Rectangle4i(from, to)

        fun fromTo(x1: Int, y1: Int, x2: Int, y2: Int) =
            fromTo(Vec2i(x1, y1), Vec2i(x2, y2))

        fun fromSize(x: Int, y: Int, w: Int, h: Int) =
            fromTo(x, y, x + w, y + h)

        fun fromSize(from: Vec2i, size: Vec2i) =
            fromSize(from.x, from.y, size.x, size.y)

        fun from(x: IntRange, y: IntRange) =
            fromSize(x.first, y.first, x.last, y.last)
    }
}