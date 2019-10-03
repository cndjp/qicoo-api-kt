package domain.repository.like_count

import domain.dao.like_count.LikeCount
import domain.model.like_count.LikeCountRow
import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
import java.util.*

class LikeCountRepositoryImpl: LikeCountRepository {
    override fun findAll(): List<LikeCount> = TODO()
    override fun findById(question_id: UUID): LikeCount? {
        val qidStr = question_id.toString()
        return RedisContext.get(qicooGlobalJedisPool.resource, qidStr)?.let {
            LikeCount(
                LikeCountRow(
                    qidStr,
                    it
                )
            )
        }
    }
}