package test.cndjp.qicoo.api.http_resource

import cndjp.qicoo.api.main
import io.ktor.application.Application
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import test.cndjp.qicoo.utils.asJsonObject
import test.cndjp.qicoo.utils.getTestResources
import kotlin.test.assertEquals

fun testGetRequestQuestion1(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Get, "/questions")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_1.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testGetRequestQuestion2(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Get, "/questions?sort=like&order=desc&per=4&page=1")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_2.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testGetRequestQuestion3(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Get, "/questions?sort=created&order=asc&per=2&page=2")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_3.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testGetRequestQuestion4(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Get, "/questions?sort=like&order=asc&per=2&page=3")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_4.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testGetRequestQuestion5(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Get, "/questions?sort=like&order=asc&per=10&page=3")) {
        assertEquals(HttpStatusCode.InternalServerError, response.status())
    }
}

fun testPostRequestQuestion1(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/questions")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPostRequestQuestion2(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/questions") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"comment": "kimetsu no yaiba"}""".toByteArray())
    } ) {
        assertEquals(HttpStatusCode.OK, response.status())
    }
}

fun testPostRequestQuestion3(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/questions") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"mayo": "雷の呼吸、一の型"}""".toByteArray())
    } ) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPostRequestAnswer1(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/questions/answer")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPostRequestAnswer2(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/questions/answer?question_id=3")) {
        assertEquals(HttpStatusCode.OK, response.status())
    }
}

fun testPostRequestAnswer3(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/questions/answer?question_id=8")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
        assertEquals("CouldNotCreateEntityFailure", response.content)
    }
}

fun testPostRequestAnswer4(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/questions/answer?question_id=hoge")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
        assertEquals("ParseRequestFailure", response.content)
    }
}

fun testPostRequestLike1(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/questions/like")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPostRequestLike2(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/questions/like?question_id=1")) {
        assertEquals(HttpStatusCode.OK, response.status())
    }
}

fun testPostRequestLike3(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/questions/like?question_id=19")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
        assertEquals("CouldNotCreateEntityFailure", response.content)
    }
}

fun testPostRequestLike4(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/questions/like?question_id=hoge")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
        assertEquals("ParseRequestFailure", response.content)
    }
}