package com.kithoc.beetroot.test

import com.kithoc.beetroot.imageprocessing.BitmapRectangleFinder
import com.kithoc.beetroot.imageprocessing.IntMatrix
import com.kithoc.beetroot.vecmath.Rectangle4i
import kotlin.test.Test
import kotlin.test.assertEquals

internal class BitmapRectangleFinderIteratorTest {
    fun test(w: Int, h: Int, i: IntArray, vararg r: Rectangle4i) {
        val img = IntMatrix(w, h, i)
        val rec = r.toList()
        assertEquals(rec, BitmapRectangleFinder.largestRectangles(w, h) { x, y -> img[x, y] != 0 }.toList())
    }

    @Test
    fun testCase1() {
        test(
            9, 7,
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
            Rectangle4i.fromSize(1, 2, 7, 3),
            // bottom-right rectangles are returned first
            Rectangle4i.fromSize(3, 5, 5, 1),
            Rectangle4i.fromSize(1, 1, 4, 1),
        )
    }
}
