package api.response.quesion

import domain.dto.question.QuestionDTO
import utils.toFomatString

data class QuestionResponse (
    val value: List<QuestionResponseUnit>
    )

data class QuestionResponseUnit (
    val program_id: String,
    val display_name: String,
    val like_count: Int,
    val comment: String,
    val created: String?,
    val updated: String?
)


fun QuestionDTO.factory(): QuestionResponse = QuestionResponse(
        value = this.questionContext.map{QuestionResponseUnit(it.program_id.value.toString(), it.display_name, it.like_count, it.comment, it.created.toFomatString(), it.updated.toFomatString())}
    )
