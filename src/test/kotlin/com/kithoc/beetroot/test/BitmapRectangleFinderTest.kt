package com.kithoc.beetroot.test

import com.kithoc.beetroot.imageprocessing.BitmapRectangleFinder
import com.kithoc.beetroot.imageprocessing.IntMatrix
import com.kithoc.beetroot.imageprocessing.IntMatrix3
import com.kithoc.beetroot.vecmath.Rectangle4i
import kotlin.test.Test
import kotlin.test.assertEquals

internal class BitmapRectangleFinderTest {
    fun test(w: Int, h: Int, lx: Int, ly: Int, lw: Int, lh: Int, i: IntArray, hs: IntArray, vararg ws: IntArray) {
        val image = IntMatrix(w, h, i)
        val expectedColumns = IntMatrix(w, h, hs)
        val expectedRectangles = IntMatrix3.from(*ws.map { IntMatrix(w, h, it) }.toTypedArray())
        val expectedLargestRectangle = Rectangle4i.fromSize(lx, ly, lw, lh)
        val (columns, largestColumn) = BitmapRectangleFinder.findColumns(
            image.width,
            image.height
        ) { x, y -> image[x, y] != 0 }
        val (rectangles, largestRectangle) = BitmapRectangleFinder.findRectangles(columns, largestColumn)
        assertEquals(expectedColumns, columns, "Columns Matrix")
        assertEquals(expectedRectangles, rectangles, "Rectangle Matrix")
        assertEquals(expectedLargestRectangle, largestRectangle, "LargestRectangle")
    }

    @Test
    fun testCase1() {
        test(
            9, 7, 1, 2, 7, 3,
            // image
            intArrayOf(
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 1, 1, 1, 1, 0, 0, 0, 0,
                0, 1, 1, 1, 1, 1, 1, 1, 0,
                0, 1, 1, 1, 1, 1, 1, 1, 0,
                0, 1, 1, 1, 1, 1, 1, 1, 0,
                0, 0, 0, 1, 1, 1, 1, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
            ),
            // heights
            intArrayOf(
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 4, 4, 5, 5, 0, 0, 0, 0,
                0, 3, 3, 4, 4, 4, 4, 4, 0,
                0, 2, 2, 3, 3, 3, 3, 3, 0,
                0, 1, 1, 2, 2, 2, 2, 2, 0,
                0, 0, 0, 1, 1, 1, 1, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
            ),
            // widths 1
            intArrayOf(
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 4, 3, 2, 1, 0, 0, 0, 0,
                0, 7, 6, 5, 4, 3, 2, 1, 0,
                0, 7, 6, 5, 4, 3, 2, 1, 0,
                0, 7, 6, 5, 4, 3, 2, 1, 0,
                0, 0, 0, 5, 4, 3, 2, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
            ),
            // widths 2
            intArrayOf(
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 4, 3, 2, 1, 0, 0, 0, 0,
                0, 7, 6, 5, 4, 3, 2, 1, 0,
                0, 7, 6, 5, 4, 3, 2, 1, 0,
                0, 0, 0, 5, 4, 3, 2, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
            ),
            // widths 3
            intArrayOf(
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 4, 3, 2, 1, 0, 0, 0, 0,
                0, 7, 6, 5, 4, 3, 2, 1, 0,
                0, 0, 0, 5, 4, 3, 2, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
            ),
            // widths 4
            intArrayOf(
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 4, 3, 2, 1, 0, 0, 0, 0,
                0, 0, 0, 5, 4, 3, 2, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
            ),
            // widths 5
            intArrayOf(
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 2, 1, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
            ),
        )
    }
}