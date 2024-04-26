package com.kithoc.beetroot.models

import com.kithoc.beetroot.json.JsonArray
import com.kithoc.beetroot.json.JsonObject
import com.kithoc.beetroot.json.mutableJsonObject
import com.kithoc.beetroot.json.toJson
import com.kithoc.beetroot.vecmath.*
import java.util.*

object ModelJson {
    @Suppress("SpellCheckingInspection")
    private object Keys {
        const val NAME = "name"
        const val TEXTURES = "textures"
        const val AO = "ao"
        const val ELEMENTS = "elements"
        const val FROM = "from"
        const val TO = "to"
        const val ROTATION = "rotation"
        const val SHADE = "shade"
        const val FACES = "faces"
        const val UV = "uv"
        const val TEXTURE = "texture"
        const val TINTINDEX = "tintindex"
        const val CULLFACE = "cullface"
        const val RESCALE = "rescale"
        const val ORIGIN = "origin"
        const val AXIS = "axis"
        const val ANGLE = "angle"
    }

    fun decodeModel(json: JsonObject): Model {
        val faces = json[Keys.ELEMENTS]?.asArray?.flatMap { decodeElement(it.asObject!!) }?.toSet() ?: setOf()
        val ambientOcclusion = json[Keys.AO]?.asBoolean ?: true
        val textures = json[Keys.TEXTURES]?.asObject?.map { (k, v) -> k to v.asString!! }?.toMap() ?: mapOf()
        return Model(
            faces = faces,
            ambientOcclusion = ambientOcclusion,
            textures = textures
        )
    }

    fun encodeModel(value: Model): JsonObject {
        val json = mutableJsonObject()
        json[Keys.ELEMENTS] = value.faces.map { encodeElement(it) }.toJson()
        json[Keys.TEXTURES] = value.textures.toJson()
        if (!value.ambientOcclusion)
            json[Keys.AO] = false
        return json.toJson()
    }

    private fun decodeElement(json: JsonObject): Set<ModelFace> {
        //   val name = json[Keys.NAME]?.asString!!
        val from = json[Keys.FROM]?.asArray?.decodeVec()!!
        val to = json[Keys.TO]?.asArray?.decodeVec()!!
        val shade = json[Keys.SHADE]?.asBoolean ?: true
        val rotation = json[Keys.ROTATION]?.asObject
        var bounds = Cuboid6f.fromTo(from, to)
        var rescale = false
        var rotationAxis = Axis3.X
        var rotationAngle = 0f
        if (rotation != null) {
            rescale = rotation[Keys.RESCALE]?.asBoolean ?: false
            if (rotation.containsKey(Keys.ANGLE) || rotation.containsKey(Keys.AXIS) || rotation.containsKey(Keys.ORIGIN)) {
                val origin = rotation[Keys.ORIGIN]?.asArray?.decodeVec()!!
                val axis = rotation[Keys.AXIS]?.asString?.decodeAxis()!!
                val angle = rotation[Keys.ANGLE]?.asFloat!!
                bounds = normalizeOrigin(bounds, origin, axis, angle)
                rotationAxis = axis
                rotationAngle = angle
            }
        }
        return json[Keys.FACES]!!.asObject!!
            .map { (direction, face) -> direction.decodeDirection()!! to face.asObject!! }
            .map { (direction, face) ->
                val texture = face[Keys.TEXTURE]?.asString!!
                val uv = face[Keys.UV]?.let { it.asArray?.decodeRect() } ?: Rectangle4f.fromTo(0f, 0f, 16f, 16f)
                val textureRotation = face[Keys.ROTATION]?.asInt?.let { TextureAngle(it) } ?: TextureAngle.VALUE_0
                val tintIndex = face[Keys.TINTINDEX]?.asInt
                val cullFace = face[Keys.CULLFACE]?.asString?.decodeDirection()
                val size = Vec2f(bounds[direction.axis.u].delta, bounds[direction.axis.v].delta)
                val position =
                    if (direction.sign == Sign.POSITIVE) bounds.min.with(direction.axis, bounds.max[direction.axis])
                    else bounds.min
                val dimension = FaceDimension(
                    direction = direction,
                    position = position,
                    size = size,
                )
                ModelFace(
                    dimension = dimension,
                    textureName = texture,
                    textureSize = uv,
                    textureRotation = textureRotation,
                    rotation = PartialRotation(rotationAxis, rotationAngle)!!,
                    rescale = rescale,
                    shade = shade,
                    cullFace = cullFace,
                    tintIndex = tintIndex
                )
            }.toSet()
    }

    private fun encodeElement(value: ModelFace): JsonObject {
        val element = mutableJsonObject()
        val bounds = value.dimension.bounds
        element[Keys.FROM] = bounds.min.encodeVec()
        element[Keys.TO] = bounds.max.encodeVec()
        if (!value.shade) {
            element[Keys.SHADE] = false
        }
        if (value.rescale || value.rotation != PartialRotation.NONE) {
            val rotation = mutableJsonObject()
            if (value.rescale) {
                rotation[Keys.RESCALE] = true
            }
            if (value.rotation != PartialRotation.NONE) {
                rotation[Keys.AXIS] = value.rotation.axis.encodeAxis()
                rotation[Keys.ANGLE] = value.rotation.angle
            }
            element[Keys.ROTATION] = rotation
        }
        val face = mutableJsonObject()
        face[Keys.UV] = value.textureSize.encodeRect()
        face[Keys.TEXTURE] = value.textureName
        if (value.textureRotation != TextureAngle.VALUE_0)
            face[Keys.ROTATION] = value.textureRotation.angle
        if (value.tintIndex != null)
            face[Keys.TINTINDEX] = value.tintIndex
        if (value.cullFace != null)
            face[Keys.CULLFACE] = value.cullFace.encodeDirection()
        element[Keys.FACES] = mapOf(value.dimension.direction.encodeDirection() to face.toJson()).toJson()
        return element.toJson()
    }

    private fun JsonArray.decodeVec(): Vec3f =
        map { it.asFloat!! }.also { if (it.size != 3) throw Exception() }.let { (x, y, z) -> Vec3f(x, y, z) }

    private fun Vec3f.encodeVec(): JsonArray =
        let { (x, y, z) -> listOf(x, y, z).toJson() }

    private fun JsonArray.decodeRect(): Rectangle4f =
        map { it.asFloat!! }.also { if (it.size != 4) throw Exception() }
            .let { (a, b, c, d) -> Rectangle4f.fromTo(a, b, c, d) }

    private fun Rectangle4f.encodeRect(): JsonArray =
        let { (min, max) -> listOf(min.x, min.y, max.x, max.y).toJson() }

    private fun String.decodeDirection(): Direction? =
        lowercase(Locale.ROOT).let { name ->
            Direction.entries.find { name == it.name.lowercase(Locale.ROOT) }
        }

    private fun Direction.encodeDirection(): String =
        name.lowercase(Locale.ROOT)

    private fun String.decodeAxis(): Axis3? =
        lowercase(Locale.ROOT).let { name ->
            Axis3.entries.find { name == it.name.lowercase(Locale.ROOT) }
        }

    private fun Axis3.encodeAxis(): String =
        name.lowercase(Locale.ROOT)

    private fun normalizeOrigin(bounds: Cuboid6f, origin: Vec3f, axis: Axis3, angle: Float): Cuboid6f {
        return bounds
        //val radians = angle / (2.0 * PI)
        //val offset = origin - origin
        //    .with(axis.u, (origin[axis.u] * cos(radians)).toFloat())
        //    .with(axis.v, (origin[axis.v] * cos(radians)).toFloat())
        //return bounds + offset
    }
}