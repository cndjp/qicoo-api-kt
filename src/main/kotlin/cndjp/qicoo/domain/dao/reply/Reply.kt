package cndjp.qicoo.domain.dao.reply

import cndjp.qicoo.domain.model.reply.reply
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

data class Reply(
    val id: EntityID<Int>,
    val question_id: EntityID<Int>,
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
