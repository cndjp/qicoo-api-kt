package cndjp.qicoo.domain.model.unlinked_user

import cndjp.qicoo.domain.model.user.user
import org.jetbrains.exposed.sql.Table

object unlinked_user : Table() {
    val user_id = (entityId("user_id",
        user
    ) references user.id).primaryKey()
    val twitter_account_id = varchar("twitter_account_id", 255).default("")
    val twitter_account_name = varchar("twitter_account_name", 255).default("")
}
