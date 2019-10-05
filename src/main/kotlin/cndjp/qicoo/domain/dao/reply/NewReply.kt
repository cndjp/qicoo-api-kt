package domain.dao.reply

import domain.model.reply.reply
import java.util.UUID
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass

class NewReply(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NewReply>(reply)

    var question_id by reply.question_id
    var comment by reply.comment
    var created by reply.created
    var updated by reply.updated
}
