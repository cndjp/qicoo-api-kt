package api.response.quesion

import domain.dto.question_aggr.QuestionListDTO
import utils.toFomatString

class QuestionResponse(questionListDTO: QuestionListDTO) {
    val program_name: String
    val event_name: String
    val display_name: String
    val like_count: Long
    val comment: String
    val created: String
    val updated: String

    init {
        this.program_name = questionListDTO.program_name
        this.event_name = questionListDTO.event_name
        this.display_name = questionListDTO.display_name
        this.like_count = questionListDTO.like_count
        this.comment = questionListDTO.comment
        this.created = questionListDTO.created.toFomatString()
        this.updated = questionListDTO.updated.toFomatString()
    }
}