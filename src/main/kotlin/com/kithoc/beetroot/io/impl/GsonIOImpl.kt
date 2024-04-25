package com.kithoc.beetroot.io.impl

import com.google.gson.Gson
import com.kithoc.beetroot.io.FileIO
import com.kithoc.beetroot.io.JsonIO
import com.kithoc.beetroot.json.*
import java.nio.file.Path
import com.google.gson.JsonArray as GsonArray
import com.google.gson.JsonElement as GsonElement
import com.google.gson.JsonNull as GsonNull
import com.google.gson.JsonObject as GsonObject
import com.google.gson.JsonPrimitive as GsonPrimitive

class GsonIOImpl(
    val fileIO: FileIO,
    val gson: Gson = Gson(),
) : JsonIO {
    override fun readJson(file: Path): Json =
        fromGson(gson.fromJson(fileIO.readFileUTF8(file), GsonElement::class.java))

    override fun writeJson(file: Path, json: Json): Unit =
        fileIO.writeFileUTF8(file).let { gson.toJson(toGson(json), it); it.flush() }

    private fun fromGson(gson: GsonElement): Json = when (gson) {
        is GsonObject -> Json(gson.asMap().map { (key, value) -> key to fromGson(value) }.toMap())
        is GsonArray -> Json(gson.map { fromGson(it) })
        is GsonNull -> Json(null)
        is GsonPrimitive -> when {
            gson.isString -> Json(gson.asJsonPrimitive.asString)
            gson.isNumber -> Json(gson.asJsonPrimitive.asNumber)
            gson.isBoolean -> Json(gson.asJsonPrimitive.asBoolean)
            else -> throw Exception()
        }

        else -> throw Exception()
    }

    private fun toGson(json: Json): GsonElement = when (json) {
        is JsonObject -> GsonObject().apply { asMap().putAll(json.map { (k, v) -> k to toGson(v) }.toMap()) }
        is JsonArray -> GsonArray().apply { asList().addAll(json.map { toGson(it) }) }
        is JsonString -> GsonPrimitive(json.value)
        is JsonNumber -> GsonPrimitive(json.value)
        is JsonBoolean -> GsonPrimitive(json.value)
        is JsonNull -> GsonNull.INSTANCE
    }
}