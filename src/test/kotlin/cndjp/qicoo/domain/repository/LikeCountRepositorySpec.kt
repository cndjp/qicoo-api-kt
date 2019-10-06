package domain.repository

import domain.dao.like_count.LikeCount
import domain.dao.like_count.LikeCountList
import domain.model.like_count.LikeCountRow
import domain.repository.like_count.LikeCountRepositoryImpl
import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
import org.spekframework.spek2.Spek
import kotlin.test.assertEquals

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
    }
})