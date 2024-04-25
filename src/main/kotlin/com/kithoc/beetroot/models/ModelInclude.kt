package com.kithoc.beetroot.models

import com.kithoc.beetroot.vecmath.Vec3f

data class ModelInclude(
    val modelName: String,
    val translation: Vec3f = Vec3f.ZERO,
    val rotation: Vec3f = Vec3f.ZERO,
    val scale: Vec3f = Vec3f.splat(1f),
    val faceFilter: ModelIncludeFilter = ModelIncludeFilter(),
)

data class ModelIncludeFilter(
    val inversion: ModelIncludeFilterInversion = ModelIncludeFilterInversion.BLACKLIST,
    val operation: ModelIncludeFilterOperation = ModelIncludeFilterOperation.AND,
    val predicates: Map<String, Set<String>> = mapOf(),
)

enum class ModelIncludeFilterInversion { WHITELIST, BLACKLIST }
enum class ModelIncludeFilterOperation { AND, OR }
