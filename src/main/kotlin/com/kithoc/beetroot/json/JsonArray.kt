package com.kithoc.beetroot.json

data class JsonArray @Deprecated("use .toJson()") constructor(
    override val value: List<Json>
) : Json, List<Json> by value {
    override fun toMutableJson() =
        MutableJsonArray(value.map { it.toMutableJson() }.toMutableList())
}

data class MutableJsonArray @Deprecated("use .toMutable()") constructor(
    val value: MutableList<MutableJson>
) : MutableJson, MutableList<MutableJson> by value {
    override fun toJson() =
        value.map { it.toJson() }.toJson()
}