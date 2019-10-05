package domain.dto.question

import java.util.UUID
import org.joda.time.DateTime

data class QuestionDTO(
    val qustion_id: Int,
    val program_name: String,
    val event_name: String,
    val display_name: String,
    val like_count: Int,
    val comment: String,
    val created: DateTime,
    val updated: DateTime
)
