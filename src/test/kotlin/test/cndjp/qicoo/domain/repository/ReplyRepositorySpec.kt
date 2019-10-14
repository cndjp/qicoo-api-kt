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
        assertEquals(0, RedisContext.zcount(qicooGlobalJedisPool.resource, "reply_list:1"))
        replyRepositoryImpl.add(1, "へいへい")
        assertEquals(1, RedisContext.zcount(qicooGlobalJedisPool.resource, "reply_list:1"))
    }
})