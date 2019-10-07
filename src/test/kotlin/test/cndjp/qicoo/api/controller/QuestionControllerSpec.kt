package test.cndjp.qicoo.api.controller

import cndjp.qicoo.api.main
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonSerializer
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.internal.Classes.getClass
import org.spekframework.spek2.Spek
import test.cndjp.qicoo.domain.repository.support.RepositorySpecSupport
import test.cndjp.qicoo.domain.repository.support.dropDummyData
import test.cndjp.qicoo.domain.repository.support.insertDummyData
import java.io.File
import java.nio.file.Paths
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.builtins.BuiltInsResourceLoader
import kotlin.test.assertEquals
import kotlin.test.assertFalse

fun testRequesetQuestion() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Get, "/questions")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            JsonParser.parseString(Paths.get("testresources/ExpectResponse_1.json").toAbsolutePath().toFile().readText()).asJsonObject,
            JsonParser.parseString(response.content).asJsonObject
        )
    }
}

object QuestionControllerSpec :Spek({
    val ss = RepositorySpecSupport
    beforeGroup {
        ss.insertDummyData()
    }
    afterGroup {
        ss.dropDummyData()
    }
    group("QuestionControllerのテスト") {
        test("GET - /questions") {
            testRequesetQuestion()
        }
    }
})