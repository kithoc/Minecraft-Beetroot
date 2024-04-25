@file:Suppress("PackageDirectoryMismatch")

package com.kithoc.beetroot.json

sealed interface Json {
    val value: Any?

    val asObject: JsonObject
        get() = (this as? JsonObject) ?: throw Exception("not an object")
    val asArray: JsonArray
        get() = (this as? JsonArray) ?: throw Exception("not an array")
    val asString: String
        get() = (this as? JsonString)?.value ?: throw Exception("not a string")
    val asBoolean: Boolean
        get() = (this as? JsonBoolean)?.value ?: throw Exception("not a boolean")
    val asDouble: Double
        get() = (this as? JsonNumber)?.value ?: throw Exception("not a string")
    val asFloat: Float
        get() = asDouble.also { if (it.toFloat().toDouble() != it) throw Exception() }.toFloat()
    val asInt: Int
        get() = asDouble.also { if (it.toInt().toDouble() != it) throw Exception() }.toInt()

    companion object {
        operator fun invoke(value: Map<String, Json>) = if (value is JsonObject) value else JsonObject(value)
        operator fun invoke(vararg value: Pair<String, Json>) = this(mapOf(*value))
        operator fun invoke(value: List<Json>) = if (value is JsonArray) value else JsonArray(value)
        operator fun invoke(vararg value: Json) = this(listOf(*value))
        operator fun invoke(value: String) = JsonString(value)
        operator fun invoke(value: Double) = JsonNumber(value)
        operator fun invoke(value: Number) = JsonNumber(value.toDouble())
        operator fun invoke(value: Boolean) = JsonBoolean(value)
        operator fun invoke(value: Nothing?) = JsonNull

        fun mutableObject(): MutableMap<String, Json> = mutableMapOf()
        fun mutableArray(): MutableList<Json> = mutableListOf()
    }
}

data class JsonObject @Deprecated("use Json(...)") constructor(
    override val value: Map<String, Json>
) : Json, Map<String, Json> by value

data class JsonArray @Deprecated("use Json(...)") constructor(
    override val value: List<Json>
) : Json, List<Json> by value

data class JsonString @Deprecated("use Json(...)") constructor(
    override val value: String
) : Json

data class JsonNumber @Deprecated("use Json(...)") constructor(
    override val value: Double
) : Json

data class JsonBoolean @Deprecated("use Json(...)") constructor(
    override val value: Boolean
) : Json

data object JsonNull : Json {
    override val value: Nothing? get() = null
}
