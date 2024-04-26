package com.kithoc.beetroot.models

import com.kithoc.beetroot.vecmath.Vec3f
import java.util.concurrent.CompletableFuture

data class Model(
    val faces: Set<ModelFace> = setOf(),
    val parentName: String? = null,
    val ambientOcclusion: Boolean = true,
    val displays: Set<ModelDisplay> = setOf(),
    val textures: Map<String, String> = mapOf(),
    val includes: Set<ModelInclude> = setOf(),
) {
    fun evaluateIncludes(io: ModelIO): CompletableFuture<Model> {
        val models = includes.map { include ->
            io.loadModel(include.modelName).thenApply { model ->
                model
                    .filterFaces(include.faceFilter)
                    .rotate(include.rotation)
                    .scale(include.scale)
                    .translate(include.translation)
            }
        }
        return CompletableFuture.allOf(*models.toTypedArray()).thenApply {
            copy(faces = models.flatMap { it.resultNow().faces }.toSet())
        }
    }

    fun combine(vararg models: Model): Model =
        copy(faces = buildSet { addAll(faces); models.forEach { addAll(it.faces) } })

    fun mapFaces(mapper: (ModelFace) -> ModelFace): Model =
        copy(faces = faces.map(mapper).toSet())

    fun flatMapFaces(mapper: (ModelFace) -> Iterable<ModelFace>): Model =
        copy(faces = faces.flatMap(mapper).toSet())

    fun flatMapFaces(filter: ModelIncludeFilter, mapper: (ModelFace) -> Iterable<ModelFace>): Model =
        copy(faces = faces.flatMap { if (it.matches(filter)) mapper(it) else listOf(it) }.toSet())

    fun filterFaces(filter: ModelIncludeFilter): Model =
        copy(faces = faces.flatMap { if (it.matches(filter)) listOf(it) else listOf() }.toSet())

    fun translate(translate: Vec3f): Model =
        if (translate == Vec3f.ZERO) this
        else mapFaces { it.translate(translate) }

    fun translate(x: Float = 0f, y: Float = 0f, z: Float = 0f): Model =
        translate(Vec3f(x, y, z))

    fun scale(scale: Vec3f, center: Vec3f = ModelConstants.VEC_CENTER): Model =
        if (scale == Vec3f.splat(1f)) this
        else mapFaces { it.scale(scale, center) }

    fun scale(x: Float = 1f, y: Float = 1f, z: Float = 1f, center: Vec3f = ModelConstants.VEC_CENTER): Model =
        scale(Vec3f(x, y, z), center)

    fun rotate(rotation: Vec3f): Model =
        if (rotation == Vec3f.ZERO) this
        else mapFaces { it.rotate(rotation) }

    fun rotate(x: Float = 0f, y: Float = 0f, z: Float = 0f): Model =
        rotate(Vec3f(x, y, z))
}