package test.cndjp.qicoo.domain.repository

import cndjp.qicoo.domain.repository.reply.ReplyRepositoryImpl
import cndjp.qicoo.infrastructure.cache.client.qicooGlobalJedisPool
import cndjp.qicoo.infrastructure.cache.context.RedisContext
import org.spekframework.spek2.Spek
import test.cndjp.qicoo.domain.repository.support.RepositorySpecSupport
import test.cndjp.qicoo.domain.repository.support.dropDummyData
import test.cndjp.qicoo.domain.repository.support.insertDummyData
import kotlin.test.assertEquals

object ReplyRepositorySpec : Spek({
    val replyRepositoryImpl = ReplyRepositoryImpl()
    val ss = RepositorySpecSupport

    beforeGroup {
        ss.insertDummyData()
    }
    afterGroup {
        ss.dropDummyData()
    }

    test("add()のテスト") {
        assertEquals(0, RedisContext.zcount(qicooGlobalJedisPool.resource, "reply_list:2"))
        replyRepositoryImpl.add(2, "へいへい")
        assertEquals(1, RedisContext.zcount(qicooGlobalJedisPool.resource, "reply_list:2"))
    }

    test("findById()のテスト") {
        val result = replyRepositoryImpl.findById(3)
        assertEquals(3, result.total)
        assertEquals(ss.q3reply1, result.list[0].comment)
        assertEquals(ss.q3reply1date, result.list[0].created.millis.toDouble())
        assertEquals(ss.q3reply2, result.list[1].comment)
        assertEquals(ss.q3reply2date, result.list[1].created.millis.toDouble())
        assertEquals(ss.q3reply3, result.list[2].comment)
        assertEquals(ss.q3reply3date, result.list[2].created.millis.toDouble())
    }
})