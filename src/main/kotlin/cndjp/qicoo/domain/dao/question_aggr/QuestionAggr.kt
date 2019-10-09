package cndjp.qicoo.domain.dao.question_aggr

import cndjp.qicoo.domain.model.event.event
import cndjp.qicoo.domain.model.program.program
import cndjp.qicoo.domain.model.question.question
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

data class QuestionAggr (
    val question_id: Int,
    val event_name: String,
    val program_name: String,
    val done_flag: Boolean,
    val display_name: String,
    val comment: String,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toQuestionAggr(): QuestionAggr =
    QuestionAggr(
        this[question.id].value,
        this[event.name],
        this[program.name],
        this[question.done_flag],
        this[question.display_name],
        this[question.comment],
        this[question.created],
        this[question.updated]
    )
