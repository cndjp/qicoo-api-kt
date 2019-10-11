package cndjp.qicoo.domain.dao.like_count

import cndjp.qicoo.domain.model.like_count.LikeCountRow

typealias LikeCountValue = Int

class LikeCount(row: LikeCountRow) {
    val question_id: Int?
    val count: LikeCountValue?

    init {
        this.question_id = row.key.toIntOrNull()
        this.count = row.value.toInt()
    }
}
