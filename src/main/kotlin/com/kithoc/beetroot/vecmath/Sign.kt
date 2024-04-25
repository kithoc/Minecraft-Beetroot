package com.kithoc.beetroot.vecmath

enum class Sign {
    NEGATIVE, POSITIVE;

    val opposite: Sign
        get() = when (this) {
            NEGATIVE -> POSITIVE
            POSITIVE -> NEGATIVE
        }
}