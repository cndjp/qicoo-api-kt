package cndjp.qicoo.domain.dao.linked_user

import cndjp.qicoo.domain.model.linked_user.linked_user
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow

data class LinkedUser(
    val user_id: EntityID<Int>,
    val github_account_name: String,
    val github_account_icon_url: String
)

fun ResultRow.toLinkedUser(): LinkedUser =
    LinkedUser(
        this[linked_user.user_id],
        this[linked_user.github_account_name],
        this[linked_user.github_account_icon_url]
    )
