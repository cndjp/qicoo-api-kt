package infrastructure

import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
import org.spekframework.spek2.Spek
import kotlin.test.assertEquals
import kotlin.test.assertNull

object RedisContextSpec: Spek({
    group("redisのコマンドテスト"){
        test("始まりのflushAll"){
            RedisContext.flushAll(qicooGlobalJedisPool.resource)
        }
        test("正常な値であれば通る"){
            RedisContext.setEx(qicooGlobalJedisPool.resource, "hoge", 1000, "piyo")
            assertEquals("piyo", RedisContext.get(qicooGlobalJedisPool.resource, "hoge"))
        }
        test("正常な値であればSET、GET、DELETEが通る"){
            RedisContext.setEx(qicooGlobalJedisPool.resource, "hoge", 1000, "piyo")
            assertEquals("piyo", RedisContext.get(qicooGlobalJedisPool.resource, "hoge"))
            assertEquals(1, RedisContext.del(qicooGlobalJedisPool.resource, "hoge"))
        }
        test("ない値であればGETはnull"){
            RedisContext.setEx(qicooGlobalJedisPool.resource, "hoge", 1000, "piyo")
            assertNull(RedisContext.get(qicooGlobalJedisPool.resource, "puga"))
        }
    }
})