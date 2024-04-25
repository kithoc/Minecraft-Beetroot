package com.kithoc.beetroot.vecmath

@JvmRecord
data class Vec2i(val x: Int, val y: Int) {
    companion object {
        val ZERO: Vec2i = Vec2i(0, 0)
    }
}