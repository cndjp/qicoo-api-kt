package main.kotlin.cndjp.qicoo.domain.entity.question

import main.kotlin.cndjp.qicoo.domain.dao.question.question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
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
