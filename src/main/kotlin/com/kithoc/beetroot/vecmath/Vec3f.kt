package com.kithoc.beetroot.vecmath

import com.kithoc.beetroot.vecmath.Axis3.*

@JvmRecord
data class Vec3f(val x: Float, val y: Float, val z: Float) {
    operator fun get(axis: Axis3): Float = when (axis) {
        X -> x
        Y -> y
        Z -> z
    }

    fun with(axis: Axis3, value: Float) = when (axis) {
        X -> copy(x = value)
        Y -> copy(y = value)
        Z -> copy(z = value)
    }

    inline fun combine(other: Vec3f, operation: (Float, Float) -> Float) =
        Vec3f(operation(this.x, other.x), operation(this.y, other.y), operation(this.z, other.z))

    operator fun plus(other: Vec3f) =
        combine(other) { a, b -> a + b }

    operator fun minus(other: Vec3f) =
        combine(other) { a, b -> a - b }

    operator fun times(other: Vec3f) =
        combine(other) { a, b -> a * b }

    companion object {
        val ZERO = Vec3f(0f, 0f, 0f)

        fun splat(v: Float) = Vec3f(v, v, v)

        fun invoke(init: (Axis3) -> Float) = Vec3f(init(X), init(Y), init(Z))
    }
}