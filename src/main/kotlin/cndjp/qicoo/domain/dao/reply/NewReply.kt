package cndjp.qicoo.domain.dao.reply

import cndjp.qicoo.domain.model.reply.reply
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class NewReply(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NewReply>(reply)

    var question_id by reply.question_id
    var comment by reply.comment
    var created by reply.created
    var updated by reply.updated
}
