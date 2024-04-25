package com.kithoc.beetroot.models

import com.kithoc.beetroot.vecmath.Vec3f

object ModelConstants {
    const val ZERO = 0f
    const val MAX = 16f

    val VEC_MAX = Vec3f.splat(MAX)
    val VEC_CENTER = Vec3f.splat(MAX / 2f)

}