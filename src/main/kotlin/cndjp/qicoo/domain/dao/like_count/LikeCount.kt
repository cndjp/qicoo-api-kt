package domain.dao.like_count

import domain.model.like_count.LikeCountRow
import java.util.UUID

class LikeCount(row: LikeCountRow) {
    val question_id: UUID?
    val count: Int?

    init {
        this.question_id = runCatching {
            UUID.fromString(row.key.rowKey.split(":").getOrNull(0))
        }.fold(
            onSuccess = { it },
            onFailure = { null }
        )
        this.count = row.value.toIntOrNull()
    }
}
