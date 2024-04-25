package com.kithoc.beetroot.io

import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer
import java.nio.file.Path

interface FileIO {
    fun readFile(file: Path): InputStream
    fun writeFile(file: Path): OutputStream
    fun readFileUTF8(file: Path): Reader
    fun writeFileUTF8(file: Path): Writer
}