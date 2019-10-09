package test.cndjp.qicoo.api.controller

import cndjp.qicoo.api.main
import cndjp.qicoo.domain.dao.done_question.toDoneQuestion
import cndjp.qicoo.domain.dao.todo_question.toTodoQuestion
import cndjp.qicoo.domain.model.done_question.done_question
import cndjp.qicoo.domain.model.todo_question.todo_question
import cndjp.qicoo.domain.repository.like_count.LikeCountRepositoryImpl
import cndjp.qicoo.infrastructure.cache.client.qicooGlobalJedisPool
import cndjp.qicoo.infrastructure.cache.context.RedisContext
import io.ktor.application.Application
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.spekframework.spek2.Spek
import test.cndjp.qicoo.api.http_resource.*
import test.cndjp.qicoo.domain.repository.support.RepositorySpecSupport
import test.cndjp.qicoo.domain.repository.support.dropDummyData
import test.cndjp.qicoo.domain.repository.support.insertDummyData
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

object QuestionControllerRealSpec :Spek({
    val ss = RepositorySpecSupport
    val engine = Application::main
    beforeGroup {
        ss.insertDummyData()
    }
    afterGroup {
        ss.dropDummyData()
    }
    group("QuestionControllerの実物データテスト") {

        group("GET :+: /api/v1/questions") {
            test("GET :+: /api/v1/questions") {
                testGetRequestQuestion1(engine)
            }
            test("GET :+: /api/v1/questions?sort=like&order=desc&per=4&page1") {
                testGetRequestQuestion2(engine)
            }
            test("GET :+: /api/v1/questions?sort=created&order=asc&per=2&page2") {
                testGetRequestQuestion3(engine)
            }
            test("GET :+: /api/v1/questions?sort=like&order=asc&per=2&page=3") {
                testGetRequestQuestion4(engine)
            }
            test("GET :+: /api/v1/questions?sort=like&order=asc&per=10&page=3") {
                testGetRequestQuestion5(engine)
            }
        }

        group("POST :+: /api/v1/questions") {
            test("POST :+: /api/v1/questions") {
                testPostRequestQuestion1(engine)
            }
            test("POST :+: /api/v1/questions :+: 正常なJSON") {
                val beforeKeys = RedisContext.zcount(qicooGlobalJedisPool.resource, LikeCountRepositoryImpl().likeCountListKey)
                transaction {
                    assertNull(todo_question.select { todo_question.comment eq "kimetsu no yaiba" }.map { it.toTodoQuestion() }.firstOrNull())
                }
                    testPostRequestQuestion2(engine)
                 transaction {
                     assertNotNull(todo_question.select { todo_question.comment eq "kimetsu no yaiba" }.map { it.toTodoQuestion() }.firstOrNull())
                 }
                val afterKeys = RedisContext.zcount(qicooGlobalJedisPool.resource, LikeCountRepositoryImpl().likeCountListKey)
                assertEquals(beforeKeys+1, afterKeys)
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
                transaction {
                    assertNotNull(todo_question.select { todo_question.question_id eq 3 }.map { it.toTodoQuestion() }.firstOrNull())
                    assertNull(done_question.select { done_question.question_id eq 3 }.map { it.toDoneQuestion() }.firstOrNull())
                }
                    testPostRequestAnswer2(engine)
                transaction {
                    assertNull(todo_question.select { todo_question.question_id eq 3 }.map { it.toTodoQuestion() }.firstOrNull())
                    assertNotNull(done_question.select { done_question.question_id eq 3 }.map { it.toDoneQuestion() }.firstOrNull())
                }
            }
            test("POST :+: /api/v1/questions/answer?question_id=8 :=: 存在しない質問へのリクエスト") {
                testPostRequestAnswer3(engine)
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
                assertEquals(ss.q1like, RedisContext.zscore(qicooGlobalJedisPool.resource, LikeCountRepositoryImpl().likeCountListKey, "1"))
                testPostRequestLike2(engine)
                assertEquals(ss.q1like+1, RedisContext.zscore(qicooGlobalJedisPool.resource, LikeCountRepositoryImpl().likeCountListKey, "1"))
            }
            test("POST :+: /api/v1/questions/like?question_id=19 :=: 存在しない質問へのリクエスト") {
                testPostRequestLike3(engine)
            }
            test("POST :+: /api/v1/questions/like?question_id=hoge :=: 異常なパラメータ") {
                testPostRequestLike4(engine)
            }
        }
    }
})