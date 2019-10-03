package domain.dto.question

import org.joda.time.DateTime

data class QuestionDTO(
    val program_name: String,
    val event_name: String,
    val display_name: String,
    val like_count: Int,
    val comment: String,
    val created: DateTime,
    val updated: DateTime
)