package infrastructure.cache.context

import redis.clients.jedis.Jedis
import redis.clients.jedis.Tuple

object RedisContext {
    fun setEx(context: Jedis, key: String, ttl: Int, value: String) = context.use { it.setex(key, ttl, value) }
    fun get(context: Jedis, key: String): String? = context.use { it.get(key) }
    fun del(context: Jedis, key: String): Long = context.use { it.del(key) }
    fun find(context: Jedis, key: String): Set<String> = context.use { it.keys(key) }
    fun incr(context: Jedis, key: String): Long = context.use { it.incr(key) }
    fun zadd(context: Jedis, key: String, value: Map<String, Double>): Long = context.use { it.zadd(key, value) }
    fun zincrby(context: Jedis, key: String, score: Double, value: String): Double = context.use { it.zincrby(key, score, value) }
    fun zrangeByScoreWithScores(context: Jedis, key: String, start: Double, end: Double): Set<Tuple> = context.use { it.zrangeByScoreWithScores(key, start, end) }
    fun zscore(context: Jedis, key: String, value: String): Double? = context.use { it.zscore(key, value) }
    fun flushAll(context: Jedis) = context.use { it.flushAll() }
}
