package api.http_resource.response.question

import domain.dto.question.QuestionDTO
import utils.toFomatString

class QuestionResponse(questionDTO: QuestionDTO) {
    val question_id: String
    val program_name: String
    val event_name: String
    val display_name: String
    val like_count: Int
    val comment: String
    val created: String
    val updated: String

    init {
        this.question_id = questionDTO.qustion_id.toString()
        this.program_name = questionDTO.program_name
        this.event_name = questionDTO.event_name
        this.display_name = questionDTO.display_name
        this.like_count = questionDTO.like_count
        this.comment = questionDTO.comment
        this.created = questionDTO.created.toFomatString()
        this.updated = questionDTO.updated.toFomatString()
    }
}
