package cndjp.qicoo.domain.dao.user

import org.joda.time.DateTime

data class NewUser(
    val github_account_name: String,
    val github_account_icon_url: String,
    val created: DateTime
)
