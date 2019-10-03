package infrastructure.cache.context

import infrastructure.cache.client.qicooGlobalJedisPoll
import redis.clients.jedis.Jedis

object RedisContext {
    fun setEx(context: Jedis, key: String, ttl: Int, value: String) = context.setex(key, ttl, value)
    fun get(context: Jedis, key: String): String = context.get(key)
    fun del(context: Jedis, key: String): Long = context.del(key)
    fun find(context: Jedis, key: String): Long = context.del(key)
}