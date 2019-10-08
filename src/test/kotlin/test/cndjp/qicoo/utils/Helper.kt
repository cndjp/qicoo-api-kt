package test.cndjp.qicoo.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.File
import java.nio.file.Paths

fun getTestResources(fileName: String): File =
    Paths.get("testresources/$fileName").toAbsolutePath().toFile()

fun String.asJsonObject(): JsonObject =
    JsonParser.parseString(this).asJsonObject
