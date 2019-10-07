package cndjp.qicoo.infrastructure.cache.client

import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig

val qicooGlobalJedisPool by lazy { JedisPool(JedisPoolConfig(), System.getenv("REDIS_HOST") ?: "localhost") }
