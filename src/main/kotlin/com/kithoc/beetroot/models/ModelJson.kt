package com.kithoc.beetroot.models

import com.kithoc.beetroot.json.Json
import com.kithoc.beetroot.json.JsonArray
import com.kithoc.beetroot.json.JsonObject
import com.kithoc.beetroot.vecmath.*
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

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
        val faces = json[Keys.ELEMENTS]?.asArray?.flatMap { decodeElement(it.asObject) }?.toSet() ?: setOf()
        val ambientOcclusion = json[Keys.AO]?.asBoolean ?: true
        val textures = json[Keys.TEXTURES]?.asObject?.map { (k, v) -> k to v.asString }?.toMap() ?: mapOf()
        return Model(
            faces = faces,
            ambientOcclusion = ambientOcclusion,
            textures = textures
        )
    }

    fun encodeModel(value: Model): JsonObject {
        val json = Json.mutableObject()
        json[Keys.ELEMENTS] = Json(value.faces.map { encodeElement(it) })
        json[Keys.TEXTURES] = Json(value.textures.map { (k, v) -> k to Json(v) }.toMap())
        if (!value.ambientOcclusion)
            json[Keys.AO] = Json(false)
        return Json(json)
    }

    private fun decodeElement(json: JsonObject): Set<ModelFace> {
        val name = json[Keys.NAME]?.asString
        val from = decodeVec(json[Keys.FROM]!!.asArray)
        val to = decodeVec(json[Keys.TO]!!.asArray)
        val shade = json[Keys.SHADE]?.asBoolean ?: true
        val rotation = json[Keys.ROTATION]?.asObject
        var bounds = Cuboid6f.fromTo(from, to)
        var rescale = false
        var rotationAxis = Axis3.X
        var rotationAngle = SpatialAngle.VALUE_0
        if (rotation != null) {
            rescale = rotation[Keys.RESCALE]?.asBoolean ?: false
            if (rotation.containsKey(Keys.ANGLE) || rotation.containsKey(Keys.AXIS) || rotation.containsKey(Keys.ORIGIN)) {
                val origin = decodeVec(rotation[Keys.ORIGIN]!!.asArray)
                val axis = decodeAxis(rotation[Keys.AXIS]!!.asString)!!
                val angle = rotation[Keys.ANGLE]!!.asFloat
                bounds = normalizeOrigin(bounds, origin, axis, angle)
                rotationAxis = axis
                rotationAngle = SpatialAngle(angle)!!
            }
        }
        return json[Keys.FACES]!!.asObject
            .map { (direction, face) -> decodeDirection(direction)!! to face.asObject }
            .map { (direction, face) ->
                val texture = face[Keys.TEXTURE]!!.asString
                val uv = face[Keys.UV]?.let { decodeRect(it.asArray) } ?: Rectangle4f.fromTo(0f, 0f, 16f, 16f)
                val textureRotation = face[Keys.ROTATION]?.let { TextureAngle(it.asInt) } ?: TextureAngle.VALUE_0
                val tintIndex = face[Keys.TINTINDEX]?.asInt
                val cullFace = face[Keys.CULLFACE]?.let { decodeDirection(it.asString) }
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
                    rotationAxis = rotationAxis,
                    rotationAngle = rotationAngle,
                    rescale = rescale,
                    shade = shade,
                    cullFace = cullFace,
                    tintIndex = tintIndex
                )
            }.toSet()
    }

    private fun encodeElement(value: ModelFace): JsonObject {
        val element = Json.mutableObject()
        val bounds = value.dimension.bounds
        element[Keys.FROM] = encodeVec(bounds.min)
        element[Keys.TO] = encodeVec(bounds.max)
        if (!value.shade) {
            element[Keys.SHADE] = Json(false)
        }
        if (value.rescale || value.rotationAngle != SpatialAngle.VALUE_0) {
            val rotation = Json.mutableObject()
            if (value.rescale) {
                rotation[Keys.RESCALE] = Json(true)
            }
            if (value.rotationAngle != SpatialAngle.VALUE_0) {
                rotation[Keys.AXIS] = Json(encodeAxis(value.rotationAxis))
                rotation[Keys.ANGLE] = Json(value.rotationAngle.angle)
            }
            element[Keys.ROTATION] = Json(rotation)
        }
        val face = Json.mutableObject()
        face[Keys.UV] = encodeRect(value.textureSize)
        face[Keys.TEXTURE] = Json(value.textureName)
        if (value.textureRotation != TextureAngle.VALUE_0)
            face[Keys.ROTATION] = Json(value.textureRotation.angle)
        if (value.tintIndex != null)
            face[Keys.TINTINDEX] = Json(value.tintIndex)
        if (value.cullFace != null)
            face[Keys.CULLFACE] = Json(encodeDirection(value.cullFace))
        element[Keys.FACES] = Json(encodeDirection(value.dimension.direction) to Json(face))
        return Json(element)
    }

    private fun decodeVec(json: JsonArray): Vec3f = json
        .map { it.asFloat }
        .also { if (it.size != 3) throw Exception() }
        .let { (x, y, z) -> Vec3f(x, y, z) }

    private fun encodeVec(value: Vec3f): JsonArray =
        value.let { (x, y, z) -> Json(Json(x), Json(y), Json(z)) }

    private fun decodeRect(json: JsonArray): Rectangle4f = json
        .map { it.asFloat }
        .also { if (it.size != 4) throw Exception() }
        .let { (a, b, c, d) -> Rectangle4f.fromTo(a, b, c, d) }

    private fun encodeRect(value: Rectangle4f): JsonArray =
        value.let { (min, max) -> Json(Json(min.x), Json(min.y), Json(max.x), Json(max.y)) }

    private fun decodeDirection(value: String): Direction? =
        value.lowercase(Locale.ROOT).let { name ->
            Direction.entries.find { name == it.name.lowercase(Locale.ROOT) }
        }

    private fun encodeDirection(value: Direction): String =
        value.name.lowercase(Locale.ROOT)

    private fun decodeAxis(value: String): Axis3? =
        value.lowercase(Locale.ROOT).let { name ->
            Axis3.entries.find { name == it.name.lowercase(Locale.ROOT) }
        }

    private fun encodeAxis(value: Axis3): String =
        value.name.lowercase(Locale.ROOT)

    private fun normalizeOrigin(bounds: Cuboid6f, origin: Vec3f, axis: Axis3, angle: Float): Cuboid6f {
        return bounds
        //val radians = angle / (2.0 * PI)
        //val offset = origin - origin
        //    .with(axis.u, (origin[axis.u] * cos(radians)).toFloat())
        //    .with(axis.v, (origin[axis.v] * cos(radians)).toFloat())
        //return bounds + offset
    }
}