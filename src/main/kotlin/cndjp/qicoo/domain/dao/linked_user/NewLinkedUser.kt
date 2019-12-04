package cndjp.qicoo.domain.dao.linked_user

import cndjp.qicoo.domain.model.linked_user.linked_user
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.insert

class NewLinkedUser(
    val user_id: EntityID<Int>,
    val github_account_name: String,
    val github_account_icon_url: String
) {
    private val f1 = user_id
    private val f2 = github_account_name
    private val f3 = github_account_icon_url
    init {
        linked_user.insert {
            it[user_id] = f1
            it[github_account_name] = f2
            it[github_account_icon_url] = f3
        }
    }
    companion object
}
