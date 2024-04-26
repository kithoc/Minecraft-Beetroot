package com.kithoc.beetroot.json

sealed interface JsonPrimitive : Json, MutableJson {
    override fun toMutableJson() = this

    override fun toJson() = this
}

data class JsonString @Deprecated("use .toJson()") constructor(override val value: String) : JsonPrimitive
data class JsonNumber @Deprecated("use .toJson()") constructor(override val value: Double) : JsonPrimitive
data class JsonBoolean @Deprecated("use .toJson()") constructor(override val value: Boolean) : JsonPrimitive

data object JsonNull : JsonPrimitive {
    override val value: Nothing? get() = null
}
