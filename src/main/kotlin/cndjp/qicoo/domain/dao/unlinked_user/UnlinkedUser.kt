package main.kotlin.cndjp.qicoo.domain.dao.unlinked_user

import main.kotlin.cndjp.qicoo.domain.dao.user.user
import org.jetbrains.exposed.sql.Table

object unlinked_user: Table() {
    val user_id = (entityId("user_id",
        user
    ) references user.id).primaryKey()
    val twitter_account_id =  varchar("twitter_account_id", 255).default("")
    val twitter_account_name =  varchar("twitter_account_name", 255).default("")
}