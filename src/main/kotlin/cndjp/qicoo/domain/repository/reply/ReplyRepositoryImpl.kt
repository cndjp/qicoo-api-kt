package cndjp.qicoo.domain.repository.reply

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.api.withLog
import cndjp.qicoo.domain.dao.reply.Reply
import cndjp.qicoo.domain.dao.reply.ReplyList
import cndjp.qicoo.domain.model.reply.ReplyRow
import cndjp.qicoo.infrastructure.cache.client.qicooGlobalJedisPool
import cndjp.qicoo.infrastructure.cache.context.RedisContext
import cndjp.qicoo.utils.getNowDateTimeJst
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result

class ReplyRepositoryImpl: ReplyRepository {
    private fun keyFactory(id: Int): String =
        "reply_list:$id"

    override fun add(id: Int, comment: String): Result<Unit, QicooError> =
        when (RedisContext.zadd(qicooGlobalJedisPool.resource, keyFactory(id), mapOf(Pair(comment, getNowDateTimeJst().millis.toDouble())))) {
            1L -> Ok(Unit)
            else -> Err(QicooError.CouldNotCreateEntityFailure.withLog())
        }

    override fun findById(id: Int): ReplyList {
        val total = RedisContext.zcount(qicooGlobalJedisPool.resource, keyFactory(id)).toInt()
        return ReplyList(
            RedisContext.zrangeByScoreWithScores(qicooGlobalJedisPool.resource, keyFactory(id), 0.0, 100000000000000.0)
                .map {
                    Reply(
                        ReplyRow(
                            it.score,
                            it.element
                        )
                    )
                }, total
        )
    }
}