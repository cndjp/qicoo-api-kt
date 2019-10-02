package api.response.quesion

import domain.dto.question.QuestionDTO
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import utils.merge
import java.util.*

data class QuestionResponse (
    val value: List<QuestionResponseUnit>
    )

data class QuestionResponseUnit (
    val program_id: EntityID<UUID>,
    val display_name: String,
    val like_count: Int,
    val comment: String,
    val created: DateTime?,
    val updated: DateTime?
)


fun QuestionDTO.factory(): QuestionResponse = QuestionResponse(
        value = this.questionContext.map{QuestionResponseUnit(it.program_id, it.display_name, it.like_count, it.comment, it.created, it.updated)}
    )
