package domain.dao.like_count

import domain.model.like_count.LikeCountRow
import java.lang.reflect.Constructor
import java.util.UUID

class LikeCount(row: LikeCountRow) {
    val question_id: UUID?
    val count: Long?

    init {
        this.question_id = runCatching { UUID.fromString(row.key)
        }.fold(
            onSuccess = { it },
            onFailure = { null }
        )
        this.count = row.key?.toLongOrNull()
    }
}