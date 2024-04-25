package com.kithoc.beetroot.models

import com.kithoc.beetroot.vecmath.*

@JvmRecord
data class ModelFace(
    val dimension: FaceDimension,
    val textureName: String,
    val textureSize: Rectangle4f,
    val textureRotation: TextureAngle,
    val rotationAxis: Axis3,
    val rotationAngle: SpatialAngle,
    val rescale: Boolean = false,
    val shade: Boolean = true,
    val cullFace: Direction? = null,
    val tintIndex: Int? = null,
    val tags: Map<String, String> = mapOf(),
) {
    fun matches(filter: ModelIncludeFilter): Boolean {
        val foundMatch = run {
            for ((key, values) in filter.predicates) {
                if (tags[key] in values) {
                    if (filter.operation == ModelIncludeFilterOperation.OR) {
                        return@run true
                    }
                } else {
                    if (filter.operation == ModelIncludeFilterOperation.AND) {
                        return@run false
                    }
                }
            }
            when (filter.operation) {
                ModelIncludeFilterOperation.OR -> false
                ModelIncludeFilterOperation.AND -> true
            }
        }
        return when (filter.inversion) {
            ModelIncludeFilterInversion.BLACKLIST -> foundMatch
            ModelIncludeFilterInversion.WHITELIST -> !foundMatch
        }
    }

    fun translate(translation: Vec3f) =
        copy(dimension = dimension.run { copy(position = position + translation) })

    fun translate(x: Float = 0f, y: Float = 0f, z: Float = 0f) =
        translate(Vec3f(x, y, z))

    fun scale(scale: Vec3f, center: Vec3f = Vec3f(8f, 8f, 8f)) =
        copy(dimension = dimension.run {
            copy(
                position = (position - center) * scale + center,
                size = Vec2f(size.x * scale[direction.axis.u], size.y * scale[direction.axis.v])
            )
        })

    fun scale(x: Float = 1f, y: Float = 1f, z: Float = 1f, center: Vec3f = Vec3f(8f, 8f, 8f)) =
        scale(Vec3f(x, y, z), center)

    fun rotate(axis: Axis3, angle: Float): ModelFace {
        var a = angle % 360f
        if (a < 0f) a += 360f
        if (a % 22.5f != 0f)
            throw IllegalArgumentException(
                "rotation angles must be a multiple of 22.5 degrees"
            )
        if (rotationAngle != SpatialAngle.VALUE_0 && angle % 90f != 0f && axis != rotationAxis)
            throw IllegalArgumentException(
                "models only support partial rotation around one axis, use `unrotate` to remove all partial rotations"
            )
        var result = this
        while (a >= 90f) {
            a -= 90f
            result = result.rotateNext(axis)
        }
        if (a != 0f) {
            result = result.copy(
                rotationAxis = axis,
                rotationAngle = SpatialAngle(rotationAngle.angle + a)!!
            )
        }
        return result
    }

    private fun rotateNext(axis: Axis3): ModelFace {
        val bounds = dimension.bounds
        val direction = dimension.direction.rotateAround(axis)
        val a = axis.rotateU
        val b = axis.rotateV
        val position = bounds.min
            .with(a, bounds.min[b])
            .with(b, ModelConstants.MAX - bounds.min[a] - bounds.size[a])
        val flipAxis = axis == dimension.direction.axis || axis == Axis3.Z
        val size =
            if (flipAxis) Vec2f(dimension.size.y, dimension.size.x)
            else dimension.size
        val cullFace = cullFace?.rotateAround(axis)
        val textureRotation =
            if (flipAxis) textureRotation.next
            else textureRotation
        return copy(
            dimension = dimension.copy(
                direction = direction,
                position = position, size = size
            ),
            cullFace = cullFace,
            textureRotation = textureRotation
        )
    }

    fun rotate(rotation: Vec3f): ModelFace {
        var self = this
        for (axis in Axis3.entries)
            if (rotation[axis] != 0f)
                self = self.rotate(axis, rotation[axis])
        return self
    }
}