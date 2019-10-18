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
    with(handleRequest(HttpMethod.Get, "/api/v1/questions")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_1.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testGetRequestQuestion2(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Get, "/api/v1/questions?sort=like&order=desc&per=4&page=1")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_2.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testGetRequestQuestion3(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Get, "/api/v1/questions?sort=created&order=asc&per=2&page=2")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_3.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testGetRequestQuestion4(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Get, "/api/v1/questions?sort=like&order=asc&per=2&page=3")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_4.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testGetRequestQuestion5(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Get, "/api/v1/questions?sort=like&order=asc&per=10&page=3")) {
        assertEquals(HttpStatusCode.InternalServerError, response.status())
    }
}

fun testPostRequestQuestion1(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/api/v1/questions")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPostRequestQuestion2(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/api/v1/questions") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"comment": "kimetsu no yaiba"}""".toByteArray())
    } ) {
        assertEquals(HttpStatusCode.OK, response.status())
    }
}

fun testPostRequestQuestion3(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/api/v1/questions") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"mayo": "雷の呼吸、一の型"}""".toByteArray())
    } ) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPutRequestAnswer1(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Put, "/api/v1/questions/answer")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPutRequestAnswer2(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Put, "/api/v1/questions/answer") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"question_id": 3}""".toByteArray())
    }) {
        assertEquals(HttpStatusCode.OK, response.status())
    }
}

fun testPutRequestAnswer3(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Put, "/api/v1/questions/answer"){
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"question_id": 8}""".toByteArray())
    }) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
        assertEquals("CouldNotCreateEntityFailure", response.content)
    }
}

fun testPutRequestAnswer4(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Put, "/api/v1/questions/answer"){
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"question_id": "魔王炎撃波"}""".toByteArray())
    }) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPutRequestLike1(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Put, "/api/v1/questions/like")) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPutRequestLike2(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Put, "/api/v1/questions/like"){
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"question_id": 1}""".toByteArray())
    }) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals(
            getTestResources("ExpectResponse_5.json").readText().asJsonObject(),
            response.content!!.asJsonObject()
        )
    }
}

fun testPutRequestLike3(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Put, "/api/v1/questions/like"){
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"question_id": 19}""".toByteArray())
    }) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
        assertEquals("NotFoundEntityFailure", response.content)
    }
}

fun testPutRequestLike4(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Put, "/api/v1/questions/like"){
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"question_id": "善治郎頑張ります"}""".toByteArray())
    }) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}

fun testPostRequestReply1(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/api/v1/questions/reply"){
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"question_id": 1, "comment": "表なら話す、裏なら話さないって決めたの"}""".toByteArray())
    }) {
        assertEquals(HttpStatusCode.OK, response.status())
    }
}

fun testPostRequestReply2(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/api/v1/questions/reply"){
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{"question_id": 19, "comment": "存在しないIDにだと・・・！？"}""".toByteArray())
    }) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
        assertEquals("NotFoundEntityFailure", response.content)
    }
}

fun testPostRequestReply3(engine: Application.() -> Unit) = withTestApplication(engine) {
    with(handleRequest(HttpMethod.Post, "/api/v1/questions/reply"){
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody("""{ "hoge": "私がいうことが全て正しい"}""".toByteArray())
    }) {
        assertEquals(HttpStatusCode.BadRequest, response.status())
    }
}