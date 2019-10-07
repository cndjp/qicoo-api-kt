package infrastructure.cache.context

import mu.KotlinLogging
import redis.clients.jedis.Jedis
import redis.clients.jedis.Tuple

object RedisContext {
    private val logger = KotlinLogging.logger {}
    fun setEx(context: Jedis, key: String, ttl: Int, value: String) = context.use {
        logger.debug("redis> SET $key $value EX $ttl")
        it.setex(key, ttl, value)
    }
    fun get(context: Jedis, key: String): String? = context.use {
        logger.debug("redis> GET $key")
        it.get(key)
    }
    fun del(context: Jedis, key: String): Long = context.use {
        logger.debug("redis> DEL $key")
        it.del(key)
    }
    fun find(context: Jedis, key: String): Set<String> = context.use {
        logger.debug("redis> KEYS $key")
        it.keys(key)
    }
    fun incr(context: Jedis, key: String): Long = context.use {
        logger.debug("redis> INCR $key")
        it.incr(key)
    }
    fun zadd(context: Jedis, key: String, value: Map<String, Double>): Long = context.use {
        logger.debug("redis> ZADD $value")
        it.zadd(key, value)
    }
    fun zincrby(context: Jedis, key: String, score: Double, value: String): Double = context.use {
        logger.debug("redis> ZINCRBY $score, $value")
        it.zincrby(key, score, value)
    }
    fun zrangeByScoreWithScores(context: Jedis, key: String, start: Double, end: Double): Set<Tuple> = context.use {
        logger.debug("redis> ZRANGEBYSCORE $key $start $end WITHSCORES")
        it.zrangeByScoreWithScores(key, start, end)
    }
    fun zscore(context: Jedis, key: String, value: String): Double? = context.use {
        logger.debug("redis> ZSCORE $key $value")
        it.zscore(key, value)
    }
    fun zcount(context: Jedis, key: String): Long = context.use {
        logger.debug("redis> ZCOUNT $key")
        it.zcount(key, "-inf", "+inf")
    }
    fun flushAll(context: Jedis) = context.use {
        logger.debug("redis> FLUSHALL")
        it.flushAll()
    }
}
