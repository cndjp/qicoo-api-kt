package cndjp.qicoo.domain.dto.reply

import org.joda.time.DateTime

data class ReplyDTO(
    val comment: String,
    val created: DateTime
)
