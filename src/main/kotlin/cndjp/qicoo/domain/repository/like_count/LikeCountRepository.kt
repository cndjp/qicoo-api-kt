package cndjp.qicoo.domain.repository.like_count

import cndjp.qicoo.domain.dao.like_count.LikeCount
import cndjp.qicoo.domain.dao.like_count.LikeCountList

interface LikeCountRepository {
    fun findAll(per: Int, page: Int, order: String): LikeCountList
    fun findById(key: Int): LikeCount?
    fun create(key: Int): Long
    fun incr(key: Int): Double
}
