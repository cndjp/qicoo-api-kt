package domain.dao.user

import domain.model.user.user
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

data class User(
    val id: EntityID<UUID>,
    val created: DateTime
)

fun ResultRow.toUser(): User =
    User(
        this[user.id],
        this[user.created]
    )