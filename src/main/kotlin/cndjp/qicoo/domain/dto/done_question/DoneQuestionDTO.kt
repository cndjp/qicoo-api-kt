package domain.dto.done_question

import domain.dao.done_question.DoneQuestion
import domain.model.done_question.done_question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

data class DoneQuestionDTOUnit(
    val dao: DoneQuestion,
    val created: DateTime?,
    val updated: DateTime?
)

class DoneQuestionDTO(
    val value: List<DoneQuestionDTOUnit>
) {
    companion object
}