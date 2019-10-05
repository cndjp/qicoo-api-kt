package domain.repository.like_count

import domain.dao.like_count.LikeCount
import domain.dao.like_count.LikeCountList
import java.util.UUID

interface LikeCountRepository {
    fun findAll(): LikeCountList
    fun findById(key: String): LikeCount?
    //fun incr(key: LikeCountRowKey)
    fun incr(key: UUID)
}
