package cndjp.qicoo.domain.dao.reply

import cndjp.qicoo.domain.model.reply.ReplyRow
import cndjp.qicoo.utils.toDateTimeFromUnixTime
import org.joda.time.DateTime

typealias ReplyValue = String

class Reply (row: ReplyRow) {
    val question_id: Int?
    val created: DateTime
    val comment: String

    init {
        this.question_id = row.key.toIntOrNull()
        this.created = row.score.toLong().toDateTimeFromUnixTime()
        this.comment = row.value
    }
}
