package domain.repository.like_count

import domain.dao.like_count.LikeCount
import domain.dao.like_count.LikeCountList
import domain.model.like_count.LikeCountRow
import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
import mu.KotlinLogging

class LikeCountRepositoryImpl : LikeCountRepository {
    private val logger = KotlinLogging.logger {}
    val likeCountListKey = "like_count_list"

    override fun findAll(per: Int, page: Int, order: String): LikeCountList {
        val total = RedisContext.zcount(qicooGlobalJedisPool.resource, likeCountListKey).toInt()
        val end = when (total > (page * per)) {
            true -> page * per
            false -> total
        }
        RedisContext.zrangeByScoreWithScores(qicooGlobalJedisPool.resource, likeCountListKey, 0.0, 100000000.0)
            .map {
                LikeCount(
                    LikeCountRow(
                        it.element,
                        it.score
                    )
                )
            }
            .let {
                when (order) {
                    "asc" -> it
                    else -> it.asReversed()
                }
            }
            .let { rowList ->
                val validPagination = kotlin.runCatching {
                    rowList.subList(((page - 1) * per), end)
                }
                validPagination.onSuccess {
                    return LikeCountList(
                        it,
                        total
                    )
                }
                return LikeCountList(
                    listOf(),
                    total
                )
            }
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
