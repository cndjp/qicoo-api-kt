package domain.repository.like_count

import domain.dao.like_count.LikeCount
import domain.model.like_count.LikeCountRow
import domain.model.like_count.LikeCountRowKey
import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext

class LikeCountRepositoryImpl : LikeCountRepository {
    override fun findAll(): List<LikeCount> = TODO()
    override fun findById(key: LikeCountRowKey): LikeCount? {
        return RedisContext.get(qicooGlobalJedisPool.resource, key.rowKey)?.let {
            LikeCount(
                LikeCountRow(
                    key,
                    it
                )
            )
        }
    }

    override fun incr(key: LikeCountRowKey) {
        RedisContext.incr(qicooGlobalJedisPool.resource, key.rowKey)
    }
}
