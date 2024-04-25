package com.kithoc.beetroot.imageprocessing

import java.util.*

class IntMatrix3(
    val width: Int,
    val height: Int,
    val depth: Int,
    val array: IntArray,
) {
    constructor(width: Int, height: Int, depth: Int) : this(width, height, depth, IntArray(width * height * depth))

    private fun getArrayIndex(x: Int, y: Int, z: Int): Int {
        Objects.checkIndex(x, width)
        Objects.checkIndex(y, height)
        Objects.checkIndex(z, depth)
        return (z * height + y) * width + x
    }

    operator fun get(x: Int, y: Int, z: Int): Int =
        array[getArrayIndex(x, y, z)]

    operator fun set(x: Int, y: Int, z: Int, v: Int) {
        array[getArrayIndex(x, y, z)] = v
    }

    fun clone(): IntMatrix3 =
        IntMatrix3(width, height, depth, array.clone())

    override fun toString(): String =
        (0..<depth).joinToString("\n], [\n", "${javaClass.simpleName}[\n", "\n]") { z ->
            (0..<height).joinToString("\n", "", "") { y ->
                (0..<width).joinToString(", ", "  [", "]") { x ->
                    this[x, y, z].toString()
                }
            }
        }

    override fun hashCode(): Int =
        17 + intArrayOf(width, height, depth, *array).reduce { a, b -> a * 31 + b }

    override fun equals(other: Any?): Boolean =
        other is IntMatrix3 &&
                width == other.width &&
                height == other.height &&
                depth == other.depth &&
                array.contentEquals(other.array)

    companion object {
        fun from(vararg matrices: IntMatrix): IntMatrix3 {
            val w = matrices.minOf { it.width }
            val h = matrices.minOf { it.height }
            if (w != matrices.maxOf { it.width } || h != matrices.maxOf { it.height }) {
                throw IllegalArgumentException()
            }
            val matrix = IntMatrix3(w, h, matrices.size)
            for (z in matrices.indices) for (x in 0..<w) for (y in 0..<h) {
                matrix[x, y, z] = matrices[z][x, y]
            }
            return matrix
        }
    }
}