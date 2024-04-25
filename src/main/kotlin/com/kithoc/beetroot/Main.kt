package com.kithoc.beetroot

import com.kithoc.beetroot.io.FileIO
import com.kithoc.beetroot.io.impl.GsonIOImpl
import com.kithoc.beetroot.models.ModelJson
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.Writer
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val baseDirectory = Path("src/test/resources/")
    val fileIO = object : FileIO {
        override fun readFile(file: Path): InputStream =
            Files.newInputStream(baseDirectory.resolve(file))

        override fun writeFile(file: Path): OutputStream =
            Files.newOutputStream(baseDirectory.resolve(file))

        override fun readFileUTF8(file: Path): Reader =
            Files.newBufferedReader(baseDirectory.resolve(file), Charsets.UTF_8)

        override fun writeFileUTF8(file: Path): Writer =
            Files.newBufferedWriter(baseDirectory.resolve(file), Charsets.UTF_8)
    }
    val jsonIO = GsonIOImpl(fileIO)
    val sourceJson = jsonIO.readJson(Path("assets/beetroot_test/models/s_face.json")).asObject
    val model = ModelJson.decodeModel(sourceJson)
    val model2 = model.copy(
        faces = model.faces +
                model.rotate(y = 90f).faces +
                model.rotate(y = 180f).faces +
                model.rotate(y = 270f).faces +
                model.rotate(z = 90f).faces +
                model.rotate(z = 270f).faces
    )
    val targetJson = ModelJson.encodeModel(model2)
    jsonIO.writeJson(Path("assets/beetroot_test/models/s_face_copy.json"), targetJson)
}