package domain.repository.like_count

import domain.dao.like_count.LikeCount
import domain.dao.like_count.LikeCountList
import domain.model.like_count.LikeCountRow
import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext

class LikeCountRepositoryImpl : LikeCountRepository {
    val likeCountListKey = "like_count_list"

    override fun findAll(): LikeCountList =
        RedisContext.zrangeByScoreWithScores(qicooGlobalJedisPool.resource, likeCountListKey, 0.0, 100000.0)
            .map { LikeCount(
                    LikeCountRow(
                        it.element,
                        it.score
                    )
                )
            }
            .let {
                LikeCountList(
                    it.asReversed(),
                    it.size
                )
            }

    override fun findById(key: Int): LikeCount? =
        RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountListKey, key.toString())?.let {
            LikeCount(
                LikeCountRow(
                    key.toString(),
                    it
                )
            )
        }

    override fun create(key: Int): Long =
        RedisContext.zadd(qicooGlobalJedisPool.resource, likeCountListKey, mapOf(Pair(key.toString(), 0.0)))


    override fun incr(key: Int): Double =
        RedisContext.zincrby(qicooGlobalJedisPool.resource, likeCountListKey, 1.0, key.toString())
}
