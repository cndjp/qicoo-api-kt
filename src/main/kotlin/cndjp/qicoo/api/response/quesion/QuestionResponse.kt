package api.response.quesion

import domain.dto.question.QuestionDTO
import utils.toFomatString

data class QuestionResponse (
    val value: List<QuestionResponseUnit>
    )

data class QuestionResponseUnit (
    val program_name: String?,
    val event_name: String?,
    val display_name: String,
    val like_count: Int,
    val comment: String,
    val created: String?,
    val updated: String?
)


fun List<QuestionDTO>.factory(): QuestionResponse = QuestionResponse(
        value = this.map{QuestionResponseUnit(it.program_name, it.event_name, it.display_name, it.like_count, it.comment, it.created?.toFomatString() ?: "", it.updated?.toFomatString() ?: "")}
    )
