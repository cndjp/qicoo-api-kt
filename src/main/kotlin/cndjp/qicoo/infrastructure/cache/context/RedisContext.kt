package infrastructure.cache.context

import redis.clients.jedis.Jedis

object RedisContext {
    fun setEx(context: Jedis, key: String, ttl: Int, value: String) = context.use{it.setex(key, ttl, value)}
    fun get(context: Jedis, key: String): String? = context.use{it.get(key)}
    fun del(context: Jedis, key: String): Long = context.use{it.del(key)}
    fun find(context: Jedis, key: String): Set<String> = context.use{it.keys(key)}
    fun flushAll(context: Jedis) = context.use{it.flushAll()}
}