package domain.repository

import domain.repository.like_count.LikeCountRepositoryImpl
import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
import org.spekframework.spek2.Spek
import kotlin.test.assertEquals
import kotlin.test.assertNull

object LikeCountRepositorySpec: Spek({
    val likeCountRepositoryImpl = LikeCountRepositoryImpl()
    beforeGroup {
        RedisContext.flushAll(qicooGlobalJedisPool.resource)
        assertEquals(3, RedisContext.zadd(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, mapOf(Pair("1", 2.0), Pair("3", 5.0), Pair("17", 4.0))))
    }
    afterGroup {
        RedisContext.flushAll(qicooGlobalJedisPool.resource)
    }

    group("LikeCountRepositoryのテスト") {
        test("findAll()のテスト"){
            val list1 = likeCountRepositoryImpl.findAll(3, 1, "desc").list
            assertEquals(3, list1[0].question_id)
            assertEquals(5, list1[0].count)
            assertEquals(17, list1[1].question_id)
            assertEquals(4, list1[1].count)
            assertEquals(1, list1[2].question_id)
            assertEquals(2, list1[2].count)
            val list2 = likeCountRepositoryImpl.findAll(3, 1, "asc").list
            assertEquals(1, list2[0].question_id)
            assertEquals(2, list2[0].count)
            assertEquals(17, list1[1].question_id)
            assertEquals(4, list1[1].count)
            assertEquals(3, list2[2].question_id)
            assertEquals(5, list2[2].count)
            val list3 = likeCountRepositoryImpl.findAll(2, 2, "desc").list
            assertEquals(1, list3[0].question_id)
            assertEquals(2, list3[0].count)

            // オーバフローなページネーションは無視して空のリストを返すこと。
            assertEquals(0, likeCountRepositoryImpl.findAll(5, 2, "desc").list.size)
            assertEquals(0, likeCountRepositoryImpl.findAll(2, 10, "desc").list.size)
        }
        test("findById()のテスト"){
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
        test("create()のテスト"){
            likeCountRepositoryImpl.create(11)
            likeCountRepositoryImpl.create(12)
            assertEquals(0.0, RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, "11"))
            assertEquals(0.0, RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, "12"))
        }
        test("incr()のテスト"){
            likeCountRepositoryImpl.incr(1)
            likeCountRepositoryImpl.incr(3)
            likeCountRepositoryImpl.incr(17)
            likeCountRepositoryImpl.incr(17)
            assertEquals(3.0, RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, "1"))
            assertEquals(6.0, RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, "3"))
            assertEquals(6.0, RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, "17"))
        }
    }
})