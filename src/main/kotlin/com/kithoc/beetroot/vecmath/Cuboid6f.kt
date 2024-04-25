package com.kithoc.beetroot.vecmath

@JvmRecord
data class Cuboid6f @Deprecated("use factories instead") constructor(
    val min: Vec3f, val max: Vec3f
) {
    val x: Range2f get() = Range2f(min.x, max.x)
    val y: Range2f get() = Range2f(min.y, max.y)
    val z: Range2f get() = Range2f(min.z, max.z)

    val size: Vec3f get() = max - min

    operator fun get(sign: Sign) = when (sign) {
        Sign.NEGATIVE -> min
        Sign.POSITIVE -> max
    }

    operator fun get(axis: Axis3) = when (axis) {
        Axis3.X -> x
        Axis3.Y -> y
        Axis3.Z -> z
    }

    operator fun plus(other: Vec3f) =
        Cuboid6f(min + other, max + other)

    companion object {
        fun fromTo(from: Vec3f, to: Vec3f) =
            Cuboid6f(from, to)

        fun fromTo(fromX: Float, fromY: Float, fromZ: Float, toX: Float, toY: Float, toZ: Float) =
            fromTo(Vec3f(fromX, fromY, fromZ), Vec3f(toX, toY, toZ))

        fun fromSize(from: Vec3f, size: Vec3f) =
            fromTo(from, from + size)

        fun fromSize(fromX: Float, fromY: Float, fromZ: Float, sizeX: Float, sizeY: Float, sizeZ: Float) =
            fromSize(Vec3f(fromX, fromY, fromZ), Vec3f(sizeX, sizeY, sizeZ))

        fun from(x: Range2f, y: Range2f, z: Range2f) =
            fromTo(x.min, y.min, z.min, x.max, y.max, z.max)
    }
}