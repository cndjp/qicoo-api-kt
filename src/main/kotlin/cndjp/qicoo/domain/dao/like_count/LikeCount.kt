package cndjp.qicoo.domain.dao.like_count

import cndjp.qicoo.domain.model.like_count.LikeCountRow

class LikeCount(row: LikeCountRow) {
    val question_id: Int?
    val count: Int?

    init {
        this.question_id = row.key.toIntOrNull()
        this.count = row.value.toInt()
    }
}
