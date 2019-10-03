package domain.dao.like_count

import domain.repository.like_count.LikeCountRow
import java.util.*
import java.util.UUID

data class LikeCount(
    val question_id: UUID?,
    val count: Long?
)

fun LikeCountRow.factory(): LikeCount = LikeCount(
    question_id = runCatching { UUID.fromString(this.key)
    }.fold(
        onSuccess = { it },
        onFailure = { null }
    ),
    count = this.value.toLongOrNull()
)