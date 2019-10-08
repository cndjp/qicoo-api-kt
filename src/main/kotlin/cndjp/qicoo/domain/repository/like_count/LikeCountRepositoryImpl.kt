package cndjp.qicoo.domain.repository.like_count

import cndjp.qicoo.domain.dao.like_count.LikeCount
import cndjp.qicoo.domain.dao.like_count.LikeCountList
import cndjp.qicoo.domain.model.like_count.LikeCountRow
import cndjp.qicoo.infrastructure.cache.client.qicooGlobalJedisPool
import cndjp.qicoo.infrastructure.cache.context.RedisContext
import cndjp.qicoo.utils.QicooError
import cndjp.qicoo.utils.QicooErrorReason
import cndjp.qicoo.utils.withLog
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import mu.KotlinLogging

class LikeCountRepositoryImpl : LikeCountRepository {
    private val logger = KotlinLogging.logger {}
    private val defaultZMin = 0.0
    private val defaultZMax = 100000000.0
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

    override fun findByIds(keys: List<Int>): LikeCountList {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun create(key: Int): Result<Unit, QicooError> =
        when (RedisContext.zadd(qicooGlobalJedisPool.resource, likeCountListKey, mapOf(Pair(key.toString(), 0.0)))) {
            1L -> Ok(Unit)
            else -> Err(QicooError(QicooErrorReason.CannotCreateEntityFailure.withLog()))
        }

    override fun incr(key: Int) =
        RedisContext.zincrby(qicooGlobalJedisPool.resource, likeCountListKey, 1.0, key.toString())
}
