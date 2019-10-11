package test.cndjp.qicoo.domain.repository

import cndjp.qicoo.domain.dao.like_count.LikeCount
import cndjp.qicoo.domain.dao.like_count.LikeCountList
import cndjp.qicoo.domain.model.like_count.LikeCountRow
import cndjp.qicoo.domain.repository.like_count.LikeCountRepository
import cndjp.qicoo.api.QicooError
import cndjp.qicoo.domain.dao.like_count.LikeCountValue
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import test.cndjp.qicoo.domain.repository.support.RepositorySpecSupport

class
LikeCountRepositoryMock : LikeCountRepository {
    private val ss = RepositorySpecSupport

    override fun findAll(per: Int, page: Int, order: String):  Result<LikeCountList, QicooError> =
        when (Triple(per, page, order)) {
            Triple(3, 1, "desc") -> Ok(LikeCountList(
                listOf(
                    LikeCount(
                        LikeCountRow(
                            "3",
                            ss.q3like
                        )
                    ),
                    LikeCount(
                        LikeCountRow(
                            "4",
                            ss.q4like
                        )
                    ),
                    LikeCount(
                        LikeCountRow(
                            "5",
                            ss.q5like
                        )
                    )
                ),
                5
            ))
            else -> TODO()
        }

    override fun findById(key: Int): LikeCount? =
        when (key) {
            1 -> LikeCount(
                LikeCountRow(
                    "1",
                    ss.q1like
                )
            )
            2 -> LikeCount(
                LikeCountRow(
                    "2",
                    ss.q2like
                )
            )
            3 -> LikeCount(
                LikeCountRow(
                    "3",
                    ss.q3like
                )
            )
            4 -> LikeCount(
                LikeCountRow(
                    "4",
                    ss.q4like
                )
            )
            5 -> LikeCount(
                LikeCountRow(
                    "5",
                    ss.q5like
                )
            )
            else -> TODO()
        }

    override fun findByIds(keys: List<Int>): LikeCountList {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(key: Int): Result<Unit, QicooError> = Ok(Unit)

    override fun incr(key: Int): Result<LikeCountValue, QicooError> = Ok(5)
}
