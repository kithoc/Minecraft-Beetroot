package com.kithoc.beetroot.models

import com.kithoc.beetroot.vecmath.Vec3f

data class ModelDisplay(
    val kind: ModelDisplayKind,
    val translation: Vec3f,
    val rotation: Vec3f,
    val scale: Vec3f,
)