package domain.model.like_count

import java.util.UUID

data class LikeCountRow (
    val key: LikeCountRowKey,
    val value: String
    )

class LikeCountRowKey(question_id: UUID){
    private val s = question_id.toString()
    val rowKey: String = "$s:like_count"
}