package com.kithoc.beetroot.test.models

import com.kithoc.beetroot.json.Json
import com.kithoc.beetroot.json.impl.GsonWrapper
import com.kithoc.beetroot.models.Model
import com.kithoc.beetroot.models.ModelJson
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelReferenceTest {
    val jsonIO = GsonWrapper()

    private fun findFile(file: String) =
        Path("./src/test/resources/assets/beetroot_test/models").resolve(file)

    private fun readJson(file: String) =
        Files.newBufferedReader(findFile(file)).use { jsonIO.decode(it) }

    private fun testFileEquals(
        expected: String,
        source: String,
        transform: (Model) -> Model
    ) {
        val e = ModelJson.decodeModel(readJson(expected).asObject!!)
        val s = ModelJson.decodeModel(readJson(source).asObject!!)
        assertEquals(e, transform(s))
    }

    @Test
    fun testRotationX() = testFileEquals("s_face_rotated_x.json", "s_face.json") { it.rotate(x = 90f) }

    @Test
    fun testRotationY() = testFileEquals("s_face_rotated_y.json", "s_face.json") { it.rotate(y = 90f) }

    @Test
    fun testRotationZ() = testFileEquals("s_face_rotated_z.json", "s_face.json") { it.rotate(z = 90f) }

    @Test
    fun testRotationFull() =
        testFileEquals("s_face_full.json", "s_face.json") {
            it.combine(
                it.rotate(y = 90f),
                it.rotate(y = 180f),
                it.rotate(y = 270f),
                it.rotate(z = 90f),
                it.rotate(z = 270f),
            )
        }

}