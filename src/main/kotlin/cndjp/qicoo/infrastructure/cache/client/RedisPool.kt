package infrastructure.cache.client

import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.JedisPool

val qicooGlobalJedisPool by lazy { JedisPool(JedisPoolConfig(), System.getenv("REDIS_HOST") ?: "localhost") }