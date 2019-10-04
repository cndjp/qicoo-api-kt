package domain.repository.like_count

import domain.dao.like_count.LikeCount
import domain.model.like_count.LikeCountRowKey

interface LikeCountRepository {
    fun findAll(): List<LikeCount>
    fun findById(key: LikeCountRowKey): LikeCount?
    fun incr(key: LikeCountRowKey)
}
