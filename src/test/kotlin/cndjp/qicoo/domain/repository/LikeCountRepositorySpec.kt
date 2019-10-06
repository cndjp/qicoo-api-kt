package domain.repository

import domain.repository.like_count.LikeCountRepositoryImpl
import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
import org.spekframework.spek2.Spek
import kotlin.test.assertEquals
import kotlin.test.assertNull

object LikeCountRepositorySpec: Spek({
    group("LikeCountRepositoryのテスト") {
        test("findAll()のテスト"){
            val likeCountRepositoryImpl = LikeCountRepositoryImpl()
            assertEquals(2, RedisContext.zadd(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, mapOf(Pair("1", 2.0), Pair("3", 5.0))))
            val list = likeCountRepositoryImpl.findAll().list
            assertEquals(3, list[0].question_id)
            assertEquals(5, list[0].count)
            assertEquals(1, list[1].question_id)
            assertEquals(2, list[1].count)
            RedisContext.flushAll(qicooGlobalJedisPool.resource)
        }
        test("findById()のテスト"){
            val likeCountRepositoryImpl = LikeCountRepositoryImpl()
            assertEquals(2, RedisContext.zadd(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, mapOf(Pair("1", 2.0), Pair("3", 5.0))))
            val like1 = likeCountRepositoryImpl.findById(1)
            val like2 = likeCountRepositoryImpl.findById(2)
            val like3 = likeCountRepositoryImpl.findById(3)
            assertNull(like2?.question_id)
            assertNull(like2?.count)
            assertEquals(1, like1?.question_id)
            assertEquals(2, like1?.count)
            assertEquals(3, like3?.question_id)
            assertEquals(5, like3?.count)
        }
    }
})