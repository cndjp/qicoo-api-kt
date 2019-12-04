package cndjp.qicoo.domain.model.linked_user

import cndjp.qicoo.domain.model.user.user
import org.jetbrains.exposed.sql.Table

object linked_user : Table() {
    val user_id = (entityId("user_id",
        user
    ) references user.id).primaryKey()
    val github_account_name = varchar("github_account_id", 255).default("").uniqueIndex()
    val github_account_icon_url = varchar("github_account_icon_url", 255).default("")
}
