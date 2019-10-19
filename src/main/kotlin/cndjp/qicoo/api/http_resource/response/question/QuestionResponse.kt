package cndjp.qicoo.api.http_resource.response.question

import cndjp.qicoo.domain.dto.question.QuestionDTO
import cndjp.qicoo.utils.toFomatString

class QuestionResponse(questionDTO: QuestionDTO) {
    val question_id: Int
    val program_name: String
    val event_name: String
    val done_flg: Boolean
    val display_name: String
    val like_count: Int
    val comment: String
    val created: String
    val updated: String
    val reply_total: Int

    init {
        this.question_id = questionDTO.question_id
        this.program_name = questionDTO.program_name
        this.event_name = questionDTO.event_name
        this.done_flg = questionDTO.done_flag
        this.display_name = questionDTO.display_name
        this.like_count = questionDTO.like_count
        this.comment = questionDTO.comment
        this.created = questionDTO.created.toFomatString()
        this.updated = questionDTO.updated.toFomatString()
        this.reply_total = questionDTO.reply_total
    }
}
