package domain.repository.like_count

import domain.dao.like_count.LikeCount
import domain.dao.like_count.LikeCountList

interface LikeCountRepository {
    fun findAll(): LikeCountList
    fun findById(key: Int): LikeCount?
    // fun incr(key: LikeCountRowKey)
    fun incr(key: Int)
    fun create(key: Int)
}
