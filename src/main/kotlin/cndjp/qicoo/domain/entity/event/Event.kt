package main.kotlin.cndjp.qicoo.domain.entity.event

import main.kotlin.cndjp.qicoo.domain.dao.event.event
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

data class Event(
    val question_id: EntityID<UUID>,
    val start_at: DateTime,
    val end_at: DateTime,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toEntity(): Event =
    Event(
        this[event.id],
        this[event.start_at],
        this[event.end_at],
        this[event.created],
        this[event.updated]
    )