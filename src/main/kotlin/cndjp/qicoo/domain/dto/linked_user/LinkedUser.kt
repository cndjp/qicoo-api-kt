package domain.dto.linked_user

import domain.model.event.event
import domain.model.linked_user.linked_user
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

data class LinkedUser(
    val user_id: EntityID<UUID>,
    val twitter_account_id: String,
    val twitter_account_name: String
)

fun ResultRow.toLinkedUser(): LinkedUser =
    LinkedUser(
        this[linked_user.user_id],
        this[linked_user.twitter_account_id],
        this[linked_user.twitter_account_name]
    )