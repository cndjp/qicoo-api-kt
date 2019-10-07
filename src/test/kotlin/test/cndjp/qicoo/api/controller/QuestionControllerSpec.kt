package test.cndjp.qicoo.api.controller

import cndjp.qicoo.api.main
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.spekframework.spek2.Spek
import kotlin.test.assertEquals
import kotlin.test.assertFalse

fun testRequesetQuestion() = withTestApplication(Application::main) {
    with(handleRequest(HttpMethod.Get, "/questions")) {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals("Hello from Ktor Testable sample application", response.content)
    }
}

object QuestionControllerSpec :Spek({
    group("QuestionControllerのテスト") {
        test("GET - /questions") {
            testRequesetQuestion()
        }
    }
})