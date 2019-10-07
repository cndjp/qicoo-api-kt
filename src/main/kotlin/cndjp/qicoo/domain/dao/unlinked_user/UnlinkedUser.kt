package cndjp.qicoo.domain.dao.unlinked_user

import cndjp.qicoo.domain.model.unlinked_user.unlinked_user
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow

data class UnlinkedUser(
    val user_id: EntityID<Int>,
    val twitter_account_id: String,
    val twitter_account_name: String
)

fun ResultRow.toUnlinkedUser(): UnlinkedUser =
    UnlinkedUser(
        this[unlinked_user.user_id],
        this[unlinked_user.twitter_account_id],
        this[unlinked_user.twitter_account_name]
    )
