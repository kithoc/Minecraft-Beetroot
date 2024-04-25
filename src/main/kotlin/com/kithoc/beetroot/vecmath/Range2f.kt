package com.kithoc.beetroot.vecmath

@JvmRecord
data class Range2f(val min: Float, val max: Float) {
    val delta: Float get() = max - min
}