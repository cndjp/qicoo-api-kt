package domain.dao.event

import domain.model.event.event
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

data class Event(
    val id: EntityID<UUID>,
    val start_at: DateTime,
    val end_at: DateTime,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toEvent(): Event =
    Event(
        this[event.id],
        this[event.start_at],
        this[event.end_at],
        this[event.created],
        this[event.updated]
    )