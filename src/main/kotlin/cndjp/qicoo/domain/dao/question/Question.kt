package cndjp.qicoo.domain.dao.question

import cndjp.qicoo.domain.model.question.question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

data class Question(
    val id: EntityID<Int>,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toQuestion(): Question =
    Question(
        this[question.id],
        this[question.created],
        this[question.updated]
    )
