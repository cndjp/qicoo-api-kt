package domain.model.linked_user

import domain.model.user.user
import org.jetbrains.exposed.sql.Table

object linked_user : Table() {
    val user_id = (entityId("user_id",
        user
    ) references user.id).primaryKey()
    val twitter_account_id = varchar("twitter_account_id", 255).default("")
    val twitter_account_name = varchar("twitter_account_name", 255).default("")
}
