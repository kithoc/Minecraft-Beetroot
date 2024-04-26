package com.kithoc.beetroot.json

import java.io.Reader
import java.io.Writer

interface JsonIO {
    fun decode(reader: Reader): Json

    fun encode(writer: Writer, json: Json)
}