package domain.dto.question_aggr

import org.joda.time.DateTime

data class QuestionDTO(
    val program_name: String,
    val event_name: String,
    val display_name: String,
    val like_count: Long,
    val comment: String,
    val created: DateTime,
    val updated: DateTime
)