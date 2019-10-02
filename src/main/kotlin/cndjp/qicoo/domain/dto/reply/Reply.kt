package domain.dto.reply

import domain.model.event.event
import domain.model.reply.reply
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

data class Reply(
    val id: EntityID<UUID>,
    val question_id: EntityID<UUID>,
    val comment: String,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toReply(): Reply =
    Reply(
        this[reply.id],
        this[reply.question_id],
        this[reply.comment],
        this[reply.created],
        this[reply.updated]
    )