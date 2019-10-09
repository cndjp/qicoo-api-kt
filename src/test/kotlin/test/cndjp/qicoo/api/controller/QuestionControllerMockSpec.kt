package test.cndjp.qicoo.api.controller

import io.ktor.application.Application
import org.spekframework.spek2.Spek
import test.cndjp.qicoo.api.http_resource.*
import test.cndjp.qicoo.api.mock

object QuestionControllerMockSpec : Spek({
    val engine = Application::mock
    group("QuestionControllerのモックテスト") {

        group("GET :+: /api/v1/questions") {
            test("GET :+: /api/v1/questions") {
                testGetRequestQuestion1(engine)
            }
            test("GET :+: /api/v1/questions?sort=like&order=desc&per=4&page1") {
                testGetRequestQuestion2(engine)
            }
        }

        group("POST :+: /api/v1/questions") {
            test("POST :+: /api/v1/questions") {
                testPostRequestQuestion1(engine)
            }
            test("POST :+: /api/v1/questions :+: 正常なJSON") {
                testPostRequestQuestion2(engine)
            }
            test("POST :+: /api/v1/questions :+: 異常なJSON") {
                testPostRequestQuestion3(engine)
            }
        }

        group("PUT :+: /api/v1/questions/answer") {
            test("PUT :+: /api/v1/questions/answer") {
                testPutRequestAnswer1(engine)
            }
            test("PUT :+: /api/v1/questions/answer :=: 正常なパラメータ") {
                testPutRequestAnswer2(engine)
            }
            test("PUT :+: /api/v1/questions/answer :=: 異常なパラメータ") {
                testPutRequestAnswer4(engine)
            }
        }

        group("PUT :+: /api/v1/questions/like") {
            test("PUT :+: /api/v1/questions/like") {
                testPutRequestLike1(engine)
            }
            test("PUT :+: /api/v1/questions/like :=: 正常なパラメータ") {
                testPutRequestLike2(engine)
            }
            test("PUT :+: /api/v1/questions/like :=: 異常なパラメータ") {
                testPutRequestLike4(engine)
            }
        }
    }
})