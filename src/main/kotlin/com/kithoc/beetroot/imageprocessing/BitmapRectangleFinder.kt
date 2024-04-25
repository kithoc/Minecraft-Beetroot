package com.kithoc.beetroot.imageprocessing

import com.kithoc.beetroot.vecmath.Rectangle4i

// Based on the algorithm described in:
//   https://cs.stackexchange.com/a/146367
object BitmapRectangleFinder {

    data class FindColumnsResult(
        val columns: IntMatrix,
        val largest: Int,
    )

    data class FindRectanglesResult(
        val rectangles: IntMatrix3,
        val largest: Rectangle4i,
    )

    fun findColumns(width: Int, height: Int, bitmap: (x: Int, y: Int) -> Boolean): FindColumnsResult {
        val heights = IntMatrix(width, height)
        var largestHeight = 0
        for (x in (0..<width).reversed()) {
            var currentHeight = 0
            for (y in (0..<height).reversed()) {
                if (bitmap(x, y)) {
                    currentHeight++
                    if (currentHeight > largestHeight) {
                        largestHeight = currentHeight
                    }
                } else {
                    currentHeight = 0
                }
                heights[x, y] = currentHeight
            }
        }
        return FindColumnsResult(
            columns = heights,
            largest = largestHeight
        )
    }

    fun findRectangles(heights: IntMatrix, largestHeight: Int): FindRectanglesResult {
        val width = heights.width
        val height = heights.height
        val widths = IntMatrix3(width, height, largestHeight)
        var largest = Rectangle4i.ZERO
        for (y in (0..<height).reversed()) {
            for (z in 0..<largestHeight) {
                var w = 0
                for (x in (0..<width).reversed()) {
                    val h = heights[x, y]
                    if (h >= z + 1) {
                        w++
                        widths[x, y, z] = w
                        if (w * h > largest.area) {
                            largest = Rectangle4i.fromSize(x, y, w, h)
                        }
                    } else {
                        w = 0
                    }
                }
            }
        }
        return FindRectanglesResult(rectangles = widths, largest = largest)
    }

    fun findRectangles(width: Int, height: Int, bitmap: (x: Int, y: Int) -> Boolean): FindRectanglesResult {
        val (columns, largestColumn) = findColumns(width, height, bitmap)
        return findRectangles(columns, largestColumn)
    }

    fun largestRectangles(width: Int, height: Int, bitmap: (x: Int, y: Int) -> Boolean): Iterable<Rectangle4i> =
        Iterable { LargestRectanglesIterator(width, height, bitmap) }

    class LargestRectanglesIterator(
        private val width: Int,
        private val height: Int,
        private val bitmap: (x: Int, y: Int) -> Boolean,
    ) : Iterator<Rectangle4i> {
        private val marked = IntMatrix(width, height)
        private var next: Rectangle4i? = null
        private var hasNext: Boolean = true

        private fun computeNext() {
            val (_, largestRectangle) = findRectangles(
                width,
                height
            ) { x, y -> marked[x, y] == 0 && bitmap(x, y) }
            if (largestRectangle.area == 0) {
                next = null
                hasNext = false
            } else {
                marked.setAll(largestRectangle) { _, _ -> 1 }
                next = largestRectangle
                hasNext = true
            }
        }

        override fun hasNext(): Boolean {
            if (!hasNext) {
                return false
            }
            if (next == null) {
                computeNext()
            }
            return hasNext
        }

        override fun next(): Rectangle4i {
            val n = next ?: throw NoSuchElementException()
            next = null
            return n
        }
    }

}