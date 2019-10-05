package domain.dao.event

import domain.model.event.event
import java.util.UUID
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

data class Event(
    val id: EntityID<Int>,
    val name: String,
    val start_at: DateTime,
    val end_at: DateTime,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toEvent(): Event =
    Event(
        this[event.id],
        this[event.name],
        this[event.start_at],
        this[event.end_at],
        this[event.created],
        this[event.updated]
    )

fun NewEvent.toEvent(): Event =
    Event(
        this.id,
        this.name,
        this.start_at,
        this.end_at,
        this.created,
        this.updated
    )
