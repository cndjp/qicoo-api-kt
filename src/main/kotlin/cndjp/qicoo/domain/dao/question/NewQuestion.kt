package domain.dao.question

import domain.model.question.question
import java.util.UUID
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.sql.ResultRow

class NewQuestion(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NewQuestion>(question)

    var created by question.created
    var updated by question.updated
}
