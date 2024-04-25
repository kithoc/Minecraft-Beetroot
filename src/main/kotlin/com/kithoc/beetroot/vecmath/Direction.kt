package com.kithoc.beetroot.vecmath

enum class Direction {
    EAST, WEST, UP, DOWN, SOUTH, NORTH;

    val axis: Axis3
        get() = when (this) {
            EAST, WEST -> Axis3.X
            UP, DOWN -> Axis3.Y
            SOUTH, NORTH -> Axis3.Z
        }

    val sign: Sign
        get() = when (this) {
            EAST, UP, SOUTH -> Sign.POSITIVE
            WEST, DOWN, NORTH -> Sign.NEGATIVE
        }

    val opposite: Direction
        get() = when (this) {
            EAST -> WEST
            WEST -> EAST
            UP -> DOWN
            DOWN -> UP
            SOUTH -> NORTH
            NORTH -> SOUTH
        }

    fun rotateAround(axis: Axis3): Direction =
        if (this.axis == axis) this
        else Direction(
            this.axis.rotateAround(axis),
            if (this.axis.rotateCausesSignFlip(axis)) sign.opposite
            else sign
        )

    companion object {
        operator fun invoke(axis: Axis3, sign: Sign): Direction =
            entries.find { it.axis == axis && it.sign == sign }!!
    }
}