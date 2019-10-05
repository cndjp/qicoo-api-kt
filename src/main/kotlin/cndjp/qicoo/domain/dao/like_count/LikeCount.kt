package domain.dao.like_count

import domain.model.like_count.LikeCountRow
import java.util.UUID

class LikeCount(row: LikeCountRow) {
    val question_id: Int
    val count: Int?

    init {
        this.question_id = row.key
        this.count = row.value.toInt()
    }
}
