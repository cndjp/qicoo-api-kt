package cndjp.qicoo.api.http_resource.response.question

import cndjp.qicoo.domain.dto.reply.ReplyDTO
import cndjp.qicoo.utils.toFomatString

class ReplyResponse(replyDTO: ReplyDTO) {
    val comment: String
    val created: String

    init {
        this.comment = replyDTO.comment
        this.created = replyDTO.created.toFomatString()
    }
}