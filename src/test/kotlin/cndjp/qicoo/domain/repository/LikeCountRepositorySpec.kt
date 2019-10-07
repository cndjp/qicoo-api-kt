package domain.repository

import domain.repository.like_count.LikeCountRepositoryImpl
import domain.repository.support.RepositorySpecSupport
import domain.repository.support.dropDummyData
import domain.repository.support.insertDummyData
import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.spekframework.spek2.Spek

object LikeCountRepositorySpec : Spek({
    val likeCountRepositoryImpl = LikeCountRepositoryImpl()
    val ss = RepositorySpecSupport
    beforeGroup {
        ss.insertDummyData()
    }
    afterGroup {
        ss.dropDummyData()
    }

    group("LikeCountRepositoryのテスト") {
        test("findAll()のテスト") {
            val list1 = likeCountRepositoryImpl.findAll(3, 1, "desc").list
            assertEquals(3, list1[0].question_id)
            assertEquals(ss.q3like.toInt(), list1[0].count)
            assertEquals(4, list1[1].question_id)
            assertEquals(ss.q4like.toInt(), list1[1].count)
            assertEquals(5, list1[2].question_id)
            assertEquals(ss.q5like.toInt(), list1[2].count)
            val list2 = likeCountRepositoryImpl.findAll(3, 1, "asc").list
            assertEquals(2, list2[0].question_id)
            assertEquals(ss.q2like.toInt(), list2[0].count)
            assertEquals(1, list2[1].question_id)
            assertEquals(ss.q1like.toInt(), list2[1].count)
            assertEquals(5, list2[2].question_id)
            assertEquals(ss.q5like.toInt(), list2[2].count)
            val list3 = likeCountRepositoryImpl.findAll(2, 2, "desc").list
            assertEquals(5, list3[0].question_id)
            assertEquals(ss.q5like.toInt(), list3[0].count)
            assertEquals(1, list3[1].question_id)
            assertEquals(ss.q1like.toInt(), list3[1].count)

            // オーバフローなページネーションは無視して空のリストを返すこと。
            assertEquals(0, likeCountRepositoryImpl.findAll(5, 2, "desc").list.size)
            assertEquals(0, likeCountRepositoryImpl.findAll(2, 10, "desc").list.size)
        }
        test("findById()のテスト") {
            val like1 = likeCountRepositoryImpl.findById(1)
            val like2 = likeCountRepositoryImpl.findById(2)
            val like9 = likeCountRepositoryImpl.findById(9)
            assertNull(like9?.question_id)
            assertNull(like9?.count)
            assertEquals(1, like1?.question_id)
            assertEquals(4, like1?.count)
            assertEquals(2, like2?.question_id)
            assertEquals(1, like2?.count)
        }
        test("create()のテスト") {
            likeCountRepositoryImpl.create(11)
            likeCountRepositoryImpl.create(12)
            assertEquals(0.0, RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, "11"))
            assertEquals(0.0, RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, "12"))
        }
        test("incr()のテスト") {
            likeCountRepositoryImpl.incr(1)
            likeCountRepositoryImpl.incr(3)
            likeCountRepositoryImpl.incr(3)
            assertEquals(5.0, RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, "1"))
            assertEquals(12.0, RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, "3"))
        }
    }
})
