package domain.repository.like_count

import domain.dao.like_count.LikeCount
import domain.dao.like_count.LikeCountList
import domain.model.like_count.LikeCountRow
import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
import java.util.UUID

class LikeCountRepositoryImpl : LikeCountRepository {
    private val likeCountListKey = "like_count_list"

    override fun findAll(): LikeCountList =
        RedisContext.zrangeByScoreWithScores(qicooGlobalJedisPool.resource, likeCountListKey, 0.0, 10000000.0)
            .map{LikeCount(
                    LikeCountRow(
                        it.element,
                        it.score
                    )
                )
            }
            .let {
                LikeCountList(
                    it,
                    it.size
                )
            }

    override fun findById(key: String): LikeCount? =
        RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountListKey, key)?.let {
            LikeCount(
                LikeCountRow(
                    key.toString(),
                    it
                )
            )
        }


    //override fun incr(key: LikeCountRowKey) {
    //    RedisContext.incr(qicooGlobalJedisPool.resource, key.rowKey)
    //}
    override fun incr(key: UUID) {
        RedisContext.zincrby(qicooGlobalJedisPool.resource, likeCountListKey, 1.0, key.toString())
    }
}
