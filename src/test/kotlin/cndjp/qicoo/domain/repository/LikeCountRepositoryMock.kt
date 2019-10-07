package domain.repository

import domain.dao.like_count.LikeCount
import domain.dao.like_count.LikeCountList
import domain.model.like_count.LikeCountRow
import domain.repository.like_count.LikeCountRepository

class LikeCountRepositoryMock: LikeCountRepository {
    val q1like = 4.0
    val q2like = 1.0
    val q3like = 10.0
    val q4like = 8.0
    val q5like = 6.0

    override fun findAll(per: Int, page: Int, order: String): LikeCountList =
        when (Triple(per, page, order)) {
            Triple(3, 1, "desc") -> LikeCountList(
                listOf(
                    LikeCount(
                        LikeCountRow(
                            "3",
                            q3like
                        )
                    ),
                    LikeCount(
                        LikeCountRow(
                            "4",
                            q4like
                        )
                    ),
                    LikeCount(
                        LikeCountRow(
                            "5",
                            q5like
                        )
                    )
                ),
                3
            )
            else -> TODO()
        }


    override fun findById(key: Int): LikeCount? =
        when (key) {
            1 -> LikeCount(
                LikeCountRow(
                    "1",
                    q1like
                )
            )
            2 -> LikeCount(
                LikeCountRow(
                    "2",
                    q2like
                )
            )
            3 -> LikeCount(
                LikeCountRow(
                    "3",
                    q3like
                )
            )
            4 -> LikeCount(
                LikeCountRow(
                    "4",
                    q4like
                )
            )
            5 -> LikeCount(
                LikeCountRow(
                    "5",
                    q5like
                )
            )
            else -> TODO()
        }

    override fun create(key: Int): Long = 1

    override fun incr(key: Int): Double = 1.0
}