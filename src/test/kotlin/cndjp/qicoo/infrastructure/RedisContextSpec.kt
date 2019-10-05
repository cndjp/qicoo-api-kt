package infrastructure

import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.spekframework.spek2.Spek
import redis.clients.jedis.Tuple

object RedisContextSpec : Spek({
    group("redisのコマンドテスト") {
        test("正常な値であればSET、GET、DELETE、INCR、ZADD、ZRANGE、ZINCRBYが通る") {
            RedisContext.setEx(qicooGlobalJedisPool.resource, "hoge1", 1000, "piyo")
            assertEquals("piyo", RedisContext.get(qicooGlobalJedisPool.resource, "hoge1"))
            assertEquals(1, RedisContext.incr(qicooGlobalJedisPool.resource, "hoge2"))
            assertEquals(1, RedisContext.del(qicooGlobalJedisPool.resource, "hoge1"))
            assertEquals(1, RedisContext.del(qicooGlobalJedisPool.resource, "hoge2"))
            assertEquals(2, RedisContext.zadd(qicooGlobalJedisPool.resource, "hoge3", mapOf(Pair("one", 1.0), Pair("two", 2.0))))
            assertEquals(4.0, RedisContext.zincrby(qicooGlobalJedisPool.resource, "hoge3", 2.0, "two"))
            assertEquals(setOf(Tuple("one", 1.0), Tuple("two", 4.0)), RedisContext.zrangeByScoreWithScores(qicooGlobalJedisPool.resource, "hoge3", 0.0, 10000.0))
            assertEquals(4.0, RedisContext.zscore(qicooGlobalJedisPool.resource, "hoge3", "two"))
            RedisContext.flushAll(qicooGlobalJedisPool.resource)
        }
        test("ない値であればGETはnull") {
            assertNull(RedisContext.get(qicooGlobalJedisPool.resource, "puga2"))
            RedisContext.flushAll(qicooGlobalJedisPool.resource)
        }
    }
})
