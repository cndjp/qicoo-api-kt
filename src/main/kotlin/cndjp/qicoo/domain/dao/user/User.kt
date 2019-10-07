package cndjp.qicoo.domain.dao.user

import cndjp.qicoo.domain.model.user.user
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

data class User(
    val id: EntityID<Int>,
    val created: DateTime
)

fun ResultRow.toUser(): User =
    User(
        this[user.id],
        this[user.created]
    )
