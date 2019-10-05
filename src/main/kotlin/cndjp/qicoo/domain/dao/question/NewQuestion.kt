package domain.dao.question

import domain.model.question.question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class NewQuestion(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NewQuestion>(question)

    var created by question.created
    var updated by question.updated
}
