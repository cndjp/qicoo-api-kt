package domain.entity.user

import domain.dao.user.user
import domain.entity.event.Event
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

data class User(
    val id: EntityID<UUID>,
    val created: DateTime
)

fun ResultRow.toEntity(): User =
    User(
        this[user.id],
        this[user.created]
    )