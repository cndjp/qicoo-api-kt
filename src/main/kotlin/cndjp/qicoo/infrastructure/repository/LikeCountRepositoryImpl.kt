package cndjp.qicoo.infrastructure.repository

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.api.withLog
import cndjp.qicoo.domain.dao.like_count.LikeCount
import cndjp.qicoo.domain.dao.like_count.LikeCountList
import cndjp.qicoo.domain.dao.like_count.LikeCountValue
import cndjp.qicoo.domain.model.like_count.LikeCountRow
import cndjp.qicoo.domain.repository.like_count.LikeCountRepository
import cndjp.qicoo.infrastructure.cache.client.qicooGlobalJedisPool
import cndjp.qicoo.infrastructure.cache.context.RedisContext
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.toResultOr

class LikeCountRepositoryImpl : LikeCountRepository {
    val likeCountListKey = "like_count_list"

    override fun findAll(per: Int, page: Int, order: String): Result<LikeCountList, QicooError> {
        val total = RedisContext.zcount(qicooGlobalJedisPool.resource, likeCountListKey).toInt()
        if (total == per && page != 1) {
            return Err(QicooError.ArrayIndexOutOfBoundsFailure.withLog())
        }
        val end = when (total > (page * per)) {
            true -> page * per
            false -> total
        }
        return RedisContext.zrangeByScoreWithScores(qicooGlobalJedisPool.resource, likeCountListKey, 0.0, 100000000.0)
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
                runCatching {
                    rowList.subList(((page - 1) * per), end)
                }.fold(
                    onSuccess = { Ok(LikeCountList(it, total)) },
                    onFailure = { Err(QicooError.ArrayIndexOutOfBoundsFailure.withLog()) }
                )
            }
    }

    override fun findById(key: Int): Result<LikeCount, QicooError> =
        RedisContext.zscore(qicooGlobalJedisPool.resource, likeCountListKey, key.toString())
            .toResultOr {
                QicooError.NotFoundEntityFailure.withLog()
            }
            .flatMap {
                Ok(LikeCount(
                    LikeCountRow(
                        key.toString(),
                        it
                    )
                ))
        }

    override fun findByIds(keys: List<Int>): LikeCountList {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun create(key: Int): Result<Unit, QicooError> =
        when (RedisContext.zadd(qicooGlobalJedisPool.resource, likeCountListKey, mapOf(Pair(key.toString(), 0.0)))) {
            1L -> Ok(Unit)
            else -> Err(QicooError.CouldNotCreateEntityFailure.withLog())
        }

    override fun incr(key: Int): Result<LikeCountValue, QicooError> =
        this.findById(key)
            .flatMap {
                Ok(RedisContext.zincrby(qicooGlobalJedisPool.resource, likeCountListKey, 1.0, key.toString()).toInt())
            }
}
