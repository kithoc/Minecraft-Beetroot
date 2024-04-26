package com.kithoc.beetroot.json

fun mutableJsonObject(): MutableJsonObject = MutableJsonObject(mutableMapOf())
fun mutableJsonArray(): MutableJsonArray = MutableJsonArray(mutableListOf())

@Suppress("UnusedReceiverParameter")
fun Nothing?.toJson(): JsonNull =
    JsonNull

fun String.toJson(): JsonString =
    JsonString(this)

fun Boolean.toJson(): JsonBoolean =
    JsonBoolean(this)

fun Number.toJson(): JsonNumber =
    JsonNumber(toDouble())

fun Map<String, Json>.toJson(): JsonObject =
    if (this is JsonObject) this else JsonObject(this)

@JvmName("toJsonFromString")
fun Map<String, String>.toJson(): JsonObject =
    this.mapValues { (_, v) -> v.toJson() }.toJson()

@JvmName("toJsonFromNumber")
fun Map<String, Number>.toJson(): JsonObject =
    this.mapValues { (_, v) -> v.toJson() }.toJson()

@JvmName("toJsonFromBoolean")
fun Map<String, Boolean>.toJson(): JsonObject =
    this.mapValues { (_, v) -> v.toJson() }.toJson()

fun Iterable<Json>.toJson(): JsonArray =
    if (this is JsonArray) this else JsonArray(toList())

@JvmName("toJsonFromString")
fun Iterable<String>.toJson(): JsonArray =
    map { it.toJson() }.toJson()

@JvmName("toJsonFromNumber")
fun Iterable<Number>.toJson(): JsonArray =
    map { it.toJson() }.toJson()

@JvmName("toJsonFromBoolean")
fun Iterable<Boolean>.toJson(): JsonArray =
    map { it.toJson() }.toJson()
