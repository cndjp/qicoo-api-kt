package cndjp.qicoo.domain.dao.program

import cndjp.qicoo.domain.model.program.program
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

data class Program(
    val id: EntityID<Int>,
    val name: String,
    val event_id: EntityID<Int>,
    val start_at: DateTime,
    val end_at: DateTime,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toProgram(): Program =
    Program(
        this[program.id],
        this[program.name],
        this[program.event_id],
        this[program.start_at],
        this[program.end_at],
        this[program.created],
        this[program.updated]
    )

fun NewProgram.toProgram(): Program =
    Program(
        this.id,
        this.name,
        this.event_id,
        this.start_at,
        this.end_at,
        this.created,
        this.updated
    )
