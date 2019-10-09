package cndjp.qicoo.domain.dao.question

import cndjp.qicoo.domain.model.question.question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

data class Question(
    val id: EntityID<Int>,
    val program_id: EntityID<Int>,
    val done_flag: Boolean,
    val display_name: String,
    val comment: String,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toQuestion(): Question =
    Question(
        this[question.id],
        this[question.program_id],
        this[question.done_flag],
        this[question.display_name],
        this[question.comment],
        this[question.created],
        this[question.updated]
    )
