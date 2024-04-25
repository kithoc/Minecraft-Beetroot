package com.kithoc.beetroot.io

import com.kithoc.beetroot.json.Json
import java.nio.file.Path

interface JsonIO {
    fun readJson(file: Path): Json
    fun writeJson(file: Path, json: Json)
}