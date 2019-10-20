package cndjp.qicoo.domain.dto.question

import cndjp.qicoo.domain.dao.reply.Reply
import org.joda.time.DateTime

data class QuestionDTO(
    val question_id: Int,
    val event_name: String,
    val program_name: String,
    val done_flag: Boolean,
    val display_name: String,
    val like_count: Int,
    val comment: String,
    val created: DateTime,
    val updated: DateTime,
    val reply_list: List<Reply>,
    val reply_total: Int
)
