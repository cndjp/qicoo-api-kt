package cndjp.qicoo.domain.dao.question

import cndjp.qicoo.domain.model.question.question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class NewQuestion(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<NewQuestion>(question)

    var program_id by question.program_id
    var done_flg by question.done_flag
    var display_name by question.display_name
    var comment by question.comment
    var created by question.created
    var updated by question.updated
}
