package domain.repository

import domain.dao.like_count.LikeCount
import domain.dao.like_count.LikeCountList
import domain.model.like_count.LikeCountRow
import domain.repository.like_count.LikeCountRepository
import domain.repository.support.RepositorySpecSupport

class LikeCountRepositoryMock : LikeCountRepository {
    private val ss = RepositorySpecSupport

    override fun findAll(per: Int, page: Int, order: String): LikeCountList =
        when (Triple(per, page, order)) {
            Triple(3, 1, "desc") -> LikeCountList(
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
            )
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

    override fun create(key: Int): Long = 1

    override fun incr(key: Int): Double = 1.0
}
