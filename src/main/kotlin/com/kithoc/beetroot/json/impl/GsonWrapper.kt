package com.kithoc.beetroot.json.impl

import com.google.gson.Gson
import com.kithoc.beetroot.json.*
import com.kithoc.beetroot.json.JsonArray
import com.kithoc.beetroot.json.JsonNull
import com.kithoc.beetroot.json.JsonObject
import java.io.Reader
import java.io.Writer
import com.google.gson.JsonArray as GsonArray
import com.google.gson.JsonElement as GsonElement
import com.google.gson.JsonNull as GsonNull
import com.google.gson.JsonObject as GsonObject
import com.google.gson.JsonPrimitive as GsonPrimitive

class GsonWrapper(val gson: Gson = Gson()) : JsonIO {
    override fun decode(reader: Reader): Json =
        convert(gson.fromJson(reader, GsonElement::class.java))

    override fun encode(writer: Writer, json: Json) =
        gson.toJson(convert(json), writer)

    private fun convert(gson: GsonElement): Json = when (gson) {
        is GsonObject -> gson.asMap().map { (key, value) -> key to convert(value) }.toMap().toJson()
        is GsonArray -> gson.map { convert(it) }.toJson()
        is GsonNull -> null.toJson()
        is GsonPrimitive -> when {
            gson.isString -> gson.asJsonPrimitive.asString.toJson()
            gson.isNumber -> gson.asJsonPrimitive.asNumber.toJson()
            gson.isBoolean -> gson.asJsonPrimitive.asBoolean.toJson()
            else -> throw Exception()
        }

        else -> throw Exception()
    }

    private fun convert(json: Json): GsonElement = when (json) {
        is JsonObject -> GsonObject().apply { asMap().putAll(json.map { (k, v) -> k to convert(v) }.toMap()) }
        is JsonArray -> GsonArray().apply { asList().addAll(json.map { convert(it) }) }
        is JsonString -> GsonPrimitive(json.value)
        is JsonNumber -> GsonPrimitive(json.value)
        is JsonBoolean -> GsonPrimitive(json.value)
        is JsonNull -> GsonNull.INSTANCE
    }
}