package com.kithoc.beetroot.json

data class JsonObject @Deprecated("use .toJson()") constructor(
    override val value: Map<String, Json>
) : Json, Map<String, Json> by value {
    override fun toMutableJson() =
        MutableJsonObject(value.mapValues { (_, v) -> v.toMutableJson() }.toMutableMap())
}

data class MutableJsonObject @Deprecated("use .toMutable()") constructor(
    val value: MutableMap<String, MutableJson>
) : MutableJson, MutableMap<String, MutableJson> by value {
    operator fun set(key: String, value: JsonPrimitive) = set(key, value as Json)
    operator fun set(key: String, value: Json) = set(key, value.toMutableJson())
    operator fun set(key: String, value: String) = set(key, value.toJson())
    operator fun set(key: String, value: Number) = set(key, value.toJson())
    operator fun set(key: String, value: Boolean) = set(key, value.toJson())
    operator fun set(key: String, value: Nothing?) = set(key, value.toJson())

    override fun toJson() =
        value.mapValues { (_, v) -> v.toJson() }.toJson()
}