package domain.dao.question

import domain.model.question.question
import java.util.UUID
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass

class NewQuestion(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<NewQuestion>(question)

    var created by question.created
    var updated by question.updated
}
