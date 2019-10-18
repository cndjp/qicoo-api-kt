package cndjp.qicoo.api.http_resource.response.question

import cndjp.qicoo.domain.dto.question_detail.QuestionDetailDTO
import cndjp.qicoo.utils.toFomatString

class QuestionDetailResponse(questionDetailDTO: QuestionDetailDTO) {
    val question_id: Int
    val program_name: String
    val event_name: String
    val done_flg: Boolean
    val display_name: String
    val like_count: Int
    val comment: String
    val created: String
    val updated: String
    val reply_list: List<ReplyResponse>
    val reply_total: Int

    init {
        this.question_id = questionDetailDTO.questionDTO.question_id
        this.program_name = questionDetailDTO.questionDTO.program_name
        this.event_name = questionDetailDTO.questionDTO.event_name
        this.done_flg = questionDetailDTO.questionDTO.done_flag
        this.display_name = questionDetailDTO.questionDTO.display_name
        this.like_count = questionDetailDTO.questionDTO.like_count
        this.comment = questionDetailDTO.questionDTO.comment
        this.created = questionDetailDTO.questionDTO.created.toFomatString()
        this.updated = questionDetailDTO.questionDTO.updated.toFomatString()
        this.reply_list = questionDetailDTO.replyListDTO.list.map {ReplyResponse(it)}
        this.reply_total = questionDetailDTO.replyListDTO.total
    }
}