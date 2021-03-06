package cndjp.qicoo.domain.repository.like_count

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.domain.dao.like_count.LikeCount
import cndjp.qicoo.domain.dao.like_count.LikeCountList
import cndjp.qicoo.domain.dao.like_count.LikeCountValue
import com.github.michaelbull.result.Result

interface LikeCountRepository {
    fun findAll(per: Int, page: Int, order: String): Result<LikeCountList, QicooError>
    fun findById(key: Int): Result<LikeCount, QicooError>
    fun findByIds(keys: List<Int>): LikeCountList
    fun create(key: Int): Result<Unit, QicooError>
    fun incr(key: Int): Result<LikeCountValue, QicooError>
}
