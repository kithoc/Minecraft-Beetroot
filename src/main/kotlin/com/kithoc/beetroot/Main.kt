package com.kithoc.beetroot

import com.kithoc.beetroot.json.Json
import com.kithoc.beetroot.json.impl.GsonWrapper
import com.kithoc.beetroot.models.ModelJson
import java.nio.file.Files
import kotlin.io.path.Path

fun main() {
    val baseDirectory = Path("src/test/resources/")
    val jsonIO = GsonWrapper()
    fun reader(path: String) = Files.newBufferedReader(baseDirectory.resolve(path), Charsets.UTF_8)
    fun writer(path: String) = Files.newBufferedWriter(baseDirectory.resolve(path), Charsets.UTF_8)
    fun readJson(path: String): Json = reader(path).use { jsonIO.decode(it) }
    fun writeJson(path: String, json: Json) = writer(path).use { jsonIO.encode(it, json); it.flush() }

    val sourceJson = readJson("assets/beetroot_test/models/s_face.json").asObject!!
    val model = ModelJson.decodeModel(sourceJson)

    val modelRotX = model.rotate(x = 90f)
    val modelRotY = model.rotate(y = 90f)
    val modelRotZ = model.rotate(z = 90f)

    val modelRotY2 = modelRotY.rotate(y = 90f)
    val modelRotY3 = modelRotY2.rotate(y = 90f)
    val modelRotZ2 = modelRotZ.rotate(z = 180f)
    val fullModel = model.combine(modelRotY, modelRotY2, modelRotY3, modelRotZ, modelRotZ2)


//    val model2 = model.copy(
//        faces = model.faces +
//                model.rotate(y = 90f).faces +
//                model.rotate(y = 180f).faces +
//                model.rotate(y = 270f).faces +
//                model.rotate(z = 90f).faces +
//                model.rotate(z = 270f).faces
//    )
    writeJson("assets/beetroot_test/models/s_face_rotated_x.json", ModelJson.encodeModel(modelRotX))
    writeJson("assets/beetroot_test/models/s_face_rotated_y.json", ModelJson.encodeModel(modelRotY))
    writeJson("assets/beetroot_test/models/s_face_rotated_z.json", ModelJson.encodeModel(modelRotZ))
    writeJson("assets/beetroot_test/models/s_face_full.json", ModelJson.encodeModel(fullModel))
}