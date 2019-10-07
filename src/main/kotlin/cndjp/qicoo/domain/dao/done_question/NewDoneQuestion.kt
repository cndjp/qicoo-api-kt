package cndjp.qicoo.domain.dao.done_question

import cndjp.qicoo.domain.model.done_question.done_question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.insert

class NewDoneQuestion(
    val question_id: EntityID<Int>,
    val program_id: EntityID<Int>,
    val display_name: String,
    val comment: String
) {
    private val f1 = question_id
    private val f2 = program_id
    private val f3 = display_name
    private val f4 = comment
    init {
        done_question.insert {
            it[question_id] = f1
            it[program_id] = f2
            it[display_name] = f3
            it[comment] = f4
        }
    }
}
