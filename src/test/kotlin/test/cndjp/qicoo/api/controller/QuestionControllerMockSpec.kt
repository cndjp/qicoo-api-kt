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

        group("POST :+: /api/v1/questions/answer") {
            test("POST :+: /api/v1/questions/answer") {
                testPostRequestAnswer1(engine)
            }
            test("POST :+: /api/v1/questions/answer?question_id=3 :=: 正常なパラメータ") {
                testPostRequestAnswer2(engine)
            }
            test("POST :+: /api/v1/questions/answer?question_id=hoge :=: 異常なパラメータ") {
                testPostRequestAnswer4(engine)
            }
        }

        group("POST :+: /api/v1/questions/like") {
            test("POST :+: /api/v1/questions/like") {
                testPostRequestLike1(engine)
            }
            test("POST :+: /api/v1/questions/like?question_id=1 :=: 正常なパラメータ") {
                testPostRequestLike2(engine)
            }
            test("POST :+: /api/v1/questions/like?question_id=hoge :=: 異常なパラメータ") {
                testPostRequestLike4(engine)
            }
        }
    }
})