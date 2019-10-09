package test.cndjp.qicoo.api.controller

import cndjp.qicoo.api.main
import cndjp.qicoo.domain.dao.question.toQuestion
import cndjp.qicoo.domain.model.question.question
import cndjp.qicoo.domain.repository.like_count.LikeCountRepositoryImpl
import cndjp.qicoo.infrastructure.cache.client.qicooGlobalJedisPool
import cndjp.qicoo.infrastructure.cache.context.RedisContext
import io.ktor.application.Application
import org.jetbrains.exposed.sql.and
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
                    assertNull(question.select { question.comment eq "kimetsu no yaiba" }.map { it.toQuestion() }.firstOrNull())
                }
                    testPostRequestQuestion2(engine)
                 transaction {
                     assertNotNull(question.select { question.comment eq "kimetsu no yaiba" }.map { it.toQuestion() }.firstOrNull())
                 }
                val afterKeys = RedisContext.zcount(qicooGlobalJedisPool.resource, LikeCountRepositoryImpl().likeCountListKey)
                assertEquals(beforeKeys+1, afterKeys)
            }
            test("POST :+: /api/v1/questions :+: 異常なJSON") {
                testPostRequestQuestion3(engine)
            }
        }

        group("PUT :+: /api/v1/questions/answer") {
            test("PUT :+: /api/v1/questions/answer") {
                testPutRequestAnswer1(engine)
            }
            test("PUT :+: /api/v1/questions/answer :=: 正常なJSON") {
                transaction {
                    assertNull(question.select { (question.id eq 3) and (question.done_flag eq true) }.map { it.toQuestion() }.firstOrNull())
                }
                    testPutRequestAnswer2(engine)
                transaction {
                    assertNotNull(question.select { (question.id eq 3) and (question.done_flag eq true) }.map { it.toQuestion() }.firstOrNull())
                }
            }
            test("PUT :+: /api/v1/questions/answer :=: 存在しない質問へのリクエスト") {
                testPutRequestAnswer3(engine)
            }
            test("PUT :+: /api/v1/questions/answer :=: 異常なJSON") {
                testPutRequestAnswer4(engine)
            }
        }

        group("PUT :+: /api/v1/questions/like") {
            test("PUT :+: /api/v1/questions/like") {
                testPutRequestLike1(engine)
            }
            test("PUT :+: /api/v1/questions/like :=: 正常なJSON") {
                assertEquals(ss.q1like, RedisContext.zscore(qicooGlobalJedisPool.resource, LikeCountRepositoryImpl().likeCountListKey, "1"))
                testPutRequestLike2(engine)
                assertEquals(ss.q1like+1, RedisContext.zscore(qicooGlobalJedisPool.resource, LikeCountRepositoryImpl().likeCountListKey, "1"))
            }
            test("PUT :+: /api/v1/questions/like :=: 存在しない質問へのリクエスト") {
                testPutRequestLike3(engine)
            }
            test("PUT :+: /api/v1/questions/like :=: 異常なJSON") {
                testPutRequestLike4(engine)
            }
        }
    }
})