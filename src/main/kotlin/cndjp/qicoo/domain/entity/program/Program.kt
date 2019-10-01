package domain.entity.program

import domain.dao.event.event
import domain.dao.program.program
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

data class Program(
    val id: EntityID<UUID>,
    val event_id: EntityID<UUID>,
    val start_at: DateTime,
    val end_at: DateTime,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toEntity(): Program =
    Program(
        this[program.id],
        this[program.event_id],
        this[program.start_at],
        this[program.end_at],
        this[program.created],
        this[program.updated]
    )