package domain.dao.question

import domain.model.question.question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

data class Question(
    val id: EntityID<UUID>,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toQuestion(): Question =
    Question(
        this[question.id],
        this[question.created],
        this[question.updated]
    )
