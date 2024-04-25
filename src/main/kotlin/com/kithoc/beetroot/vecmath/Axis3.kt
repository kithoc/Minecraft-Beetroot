package com.kithoc.beetroot.vecmath

enum class Axis3 {
    X, Y, Z;

    val positive: Direction
        get() = when (this) {
            X -> Direction.EAST
            Y -> Direction.UP
            Z -> Direction.SOUTH
        }

    val negative: Direction
        get() = when (this) {
            X -> Direction.WEST
            Y -> Direction.DOWN
            Z -> Direction.NORTH
        }

    val u: Axis3
        get() = when (this) {
            X -> Z
            Y -> X
            Z -> X
        }

    val v: Axis3
        get() = when (this) {
            X -> Y
            Y -> Z
            Z -> Y
        }

    val rotateU: Axis3
        get() = when (this) {
            X -> Z
            Y -> X
            Z -> Y
        }

    val rotateV: Axis3
        get() = when (this) {
            X -> Y
            Y -> Z
            Z -> X
        }

    operator fun get(sign: Sign) = when (sign) {
        Sign.NEGATIVE -> negative
        Sign.POSITIVE -> positive
    }

    fun rotateAround(axis: Axis3): Axis3 =
        if (axis == this) this
        else Axis3.entries.find { it != this && it != axis }!!


    fun rotateCausesSignFlip(axis: Axis3): Boolean =
        when (this) {
            X -> axis == Y
            Y -> axis == Z
            Z -> axis == X
        }
}