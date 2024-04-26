package com.kithoc.beetroot.json

sealed interface Json {
    val value: Any?

    fun toMutableJson(): MutableJson

    fun toJson(): Json = this

    val asObject: JsonObject?
        get() = this as? JsonObject

    val asArray: JsonArray?
        get() = this as? JsonArray

    val asString: String?
        get() = (this as? JsonString)?.value

    val asBoolean: Boolean?
        get() = (this as? JsonBoolean)?.value

    val asDouble: Double?
        get() = (this as? JsonNumber)?.value

    val asFloat: Float?
        get() = asDouble?.also { if (it.toFloat().toDouble() != it) throw Exception() }?.toFloat()

    val asInt: Int?
        get() = asDouble?.also { if (it.toInt().toDouble() != it) throw Exception() }?.toInt()
}

sealed interface MutableJson {
    fun toJson(): Json

    fun toMutableJson(): MutableJson = this
}
