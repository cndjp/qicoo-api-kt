package test.cndjp.qicoo.infrastructure

import cndjp.qicoo.infrastructure.repository.LikeCountRepositoryImpl
import test.cndjp.qicoo.infrastructure.support.migration_run.RepositorySpecSupport
import test.cndjp.qicoo.infrastructure.support.migration_run.dropDummyData
import test.cndjp.qicoo.infrastructure.support.migration_run.insertDummyData
import cndjp.qicoo.infrastructure.cache.client.qicooGlobalJedisPool
import cndjp.qicoo.infrastructure.cache.context.RedisContext
import cndjp.qicoo.api.QicooError
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import com.github.michaelbull.result.getError
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
            val list1 = likeCountRepositoryImpl.findAll(3, 1, "desc").get()!!.list
            assertEquals(3, list1[0].question_id)
            assertEquals(ss.q3like.toInt(), list1[0].count)
            assertEquals(4, list1[1].question_id)
            assertEquals(ss.q4like.toInt(), list1[1].count)
            assertEquals(5, list1[2].question_id)
            assertEquals(ss.q5like.toInt(), list1[2].count)
            val list2 = likeCountRepositoryImpl.findAll(3, 1, "asc").get()!!.list
            assertEquals(2, list2[0].question_id)
            assertEquals(ss.q2like.toInt(), list2[0].count)
            assertEquals(1, list2[1].question_id)
            assertEquals(ss.q1like.toInt(), list2[1].count)
            assertEquals(5, list2[2].question_id)
            assertEquals(ss.q5like.toInt(), list2[2].count)
            val list3 = likeCountRepositoryImpl.findAll(2, 2, "desc").get()!!.list
            assertEquals(5, list3[0].question_id)
            assertEquals(ss.q5like.toInt(), list3[0].count)
            assertEquals(1, list3[1].question_id)
            assertEquals(ss.q1like.toInt(), list3[1].count)

            assertEquals(QicooError.ArrayIndexOutOfBoundsFailure, likeCountRepositoryImpl.findAll(5, 2, "desc").getError())
            assertEquals(QicooError.ArrayIndexOutOfBoundsFailure, likeCountRepositoryImpl.findAll(2, 10, "desc").getError())
        }
        test("findById()のテスト") {
            val like1 = likeCountRepositoryImpl.findById(1)
            val like2 = likeCountRepositoryImpl.findById(2)
            val like9 = likeCountRepositoryImpl.findById(9)
            assertNull(like9.get())
            assertEquals(1, like1.get()!!.question_id)
            assertEquals(4, like1.get()!!.count)
            assertEquals(2, like2.get()!!.question_id)
            assertEquals(1, like2.get()!!.count)
        }
        test("create()のテスト") {
            likeCountRepositoryImpl.create(11).get()!!
            likeCountRepositoryImpl.create(12).get()!!
            assertEquals(0.0, RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, "11"))
            assertEquals(0.0, RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountRepositoryImpl.likeCountListKey, "12"))
        }
        test("incr()のテスト") {
            assertEquals(Ok(5), likeCountRepositoryImpl.incr(1))
            assertEquals(Ok(11), likeCountRepositoryImpl.incr(3))
            assertEquals(Ok(12), likeCountRepositoryImpl.incr(3))
        }
    }
})
