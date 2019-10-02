package domain.dao.unlinked_user

import domain.model.unlinked_user.unlinked_user
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.insert
import java.util.*

class NewUnlinkedUser(
    val user_id: EntityID<UUID>,
    val twitter_account_id: String,
    val twitter_account_name: String
) {
    private val f1 = user_id
    private val f2 = twitter_account_id
    private val f3 = twitter_account_name
    init {
        unlinked_user.insert {
            it[user_id] = f1
            it[twitter_account_id] = f2
            it[twitter_account_name] = f3
        }
    }
    companion object
}