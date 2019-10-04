package infrastructure

import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.spekframework.spek2.Spek

object RedisContextSpec : Spek({
    group("redisのコマンドテスト") {
        test("正常な値であればSET、GET、DELETE、INCRが通る") {
            RedisContext.setEx(qicooGlobalJedisPool.resource, "hoge1", 1000, "piyo")
            assertEquals("piyo", RedisContext.get(qicooGlobalJedisPool.resource, "hoge1"))
            assertEquals(1, RedisContext.incr(qicooGlobalJedisPool.resource, "hoge2"))
            assertEquals(1, RedisContext.del(qicooGlobalJedisPool.resource, "hoge1"))
            assertEquals(1, RedisContext.del(qicooGlobalJedisPool.resource, "hoge2"))
            RedisContext.flushAll(qicooGlobalJedisPool.resource)
        }
        test("ない値であればGETはnull") {
            assertNull(RedisContext.get(qicooGlobalJedisPool.resource, "puga2"))
            RedisContext.flushAll(qicooGlobalJedisPool.resource)
        }
    }
})
