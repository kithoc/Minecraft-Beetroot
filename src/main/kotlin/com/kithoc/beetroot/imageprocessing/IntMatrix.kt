package com.kithoc.beetroot.imageprocessing

import com.kithoc.beetroot.vecmath.Rectangle4i
import java.util.*

class IntMatrix(
    val width: Int,
    val height: Int,
    val array: IntArray,
) {
    constructor(width: Int, height: Int) : this(width, height, { _, _ -> 0 })

    constructor(width: Int, height: Int, init: (Int, Int) -> Int) :
            this(width, height, IntArray(width * height) {
                init(it % width, it / width)
            })

    init {
        assert(array.size == width * height)
    }

    private fun getArrayIndex(x: Int, y: Int): Int {
        Objects.checkIndex(x, width)
        Objects.checkIndex(y, height)
        return y * width + x
    }

    operator fun get(x: Int, y: Int): Int =
        array[getArrayIndex(x, y)]

    operator fun set(x: Int, y: Int, v: Int) {
        array[getArrayIndex(x, y)] = v
    }

    fun setAll(rect: Rectangle4i, value: (x: Int, y: Int) -> Int) {
        for (x in rect.min.x..<rect.max.x) for (y in rect.min.y..<rect.max.y) {
            this[x, y] = value(x, y)
        }
    }

    fun clone(): IntMatrix =
        IntMatrix(width, height, array.clone())

    override fun toString(): String =
        (0..<height).joinToString("\n", "${javaClass.simpleName}[\n", "\n]") { y ->
            (0..<width).joinToString(", ", "  [", "]") { x ->
                this[x, y].toString()
            }
        }

    override fun hashCode(): Int =
        17 + intArrayOf(width, height, *array).reduce { a, b -> a * 31 + b }

    override fun equals(other: Any?): Boolean =
        other is IntMatrix &&
                width == other.width &&
                height == other.height &&
                array.contentEquals(other.array)
}

