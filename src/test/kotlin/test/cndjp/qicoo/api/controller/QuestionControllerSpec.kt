package test.cndjp.qicoo.api.controller

import cndjp.qicoo.api.main
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSerializer
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.internal.Classes.getClass
import org.spekframework.spek2.Spek
import test.cndjp.qicoo.domain.repository.support.RepositorySpecSupport
import test.cndjp.qicoo.domain.repository.support.dropDummyData
import test.cndjp.qicoo.domain.repository.support.insertDummyData
import test.cndjp.qicoo.utils.asJsonObject
import test.cndjp.qicoo.utils.getTestResources
import java.io.File
import java.nio.file.Paths
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.builtins.BuiltInsResourceLoader
import kotlin.test.assertEquals
import kotlin.test.assertFalse

fun testGetRequestQuestion1() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Get, "/questions")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_1.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testGetRequestQuestion2() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Get, "/questions?sort=like&order=desc&per=4&page=1")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_2.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testGetRequestQuestion3() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Get, "/questions?sort=created&order=asc&per=2&page=2")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_3.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testGetRequestQuestion4() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Get, "/questions?sort=like&order=asc&per=2&page=3")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_4.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testGetRequestQuestion5() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Get, "/questions?sort=like&order=asc&per=10&page=3")) {
        assertEquals(HttpStatusCode.InternalServerError, response.status())
    }
}

fun testPostRequestQuestion1() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Post, "/questions")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPostRequestQuestion2() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Post, "/questions") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"comment": "全集中の呼吸"}""".toByteArray())
    } ) {
        assertEquals(HttpStatusCode.OK, response.status())
    }
}

fun testPostRequestQuestion3() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Post, "/questions") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"mayo": "雷の呼吸、一の型"}""".toByteArray())
    } ) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPostRequestAnswer1() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Post, "/questions/answer")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPostRequestAnswer2() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Post, "/questions/answer?question_id=3")) {
        assertEquals(HttpStatusCode.OK, response.status())
    }
}

fun testPostRequestAnswer3() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Post, "/questions/answer?question_id=8")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
        assertEquals("CouldNotCreateEntityFailure", response.content)
    }
}

fun testPostRequestAnswer4() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Post, "/questions/answer?question_id=hoge")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
        assertEquals("ParseRequestFailure", response.content)
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

        group("GET :+: /questions") {
            test("GET :+: /questions") {
                testGetRequestQuestion1()
            }
            test("GET :+: /questions?sort=like&order=desc&per=4&page1") {
                testGetRequestQuestion2()
            }
            test("GET :+: /questions?sort=created&order=asc&per=2&page2") {
                testGetRequestQuestion3()
            }
            test("GET :+: /questions?sort=like&order=asc&per=2&page=3") {
                testGetRequestQuestion4()
            }
            test("GET :+: /questions?sort=like&order=asc&per=2&page=3") {
                testGetRequestQuestion5()
            }
        }

        group("POST :+: /questions") {
            test("POST :+: /questions") {
                testPostRequestQuestion1()
            }
            test("POST :+: /questions :+: 正常なJSON") {
                testPostRequestQuestion2()
            }
            test("POST :+: /questions :+: 異常なJSON") {
                testPostRequestQuestion3()
            }
        }

        group("POST :+: /questions/answer") {
            test("POST :+: /questions/answer") {
                testPostRequestAnswer1()
            }
            test("POST :+: /questions/answer?question_id=3 :=: 正常なパラメータ") {
                testPostRequestAnswer2()
            }
            test("POST :+: /questions/answer?question_id=8 :=: 存在しない質問へのリクエスト") {
                testPostRequestAnswer3()
            }
            test("POST :+: /questions/answer?question_id=hoge :=: 異常なパラメータ") {
                testPostRequestAnswer4()
            }
        }
    }
})