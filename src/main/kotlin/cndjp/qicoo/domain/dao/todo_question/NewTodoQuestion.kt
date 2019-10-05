package domain.dao.todo_question

import domain.model.todo_question.todo_question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.insert

class NewTodoQuestion(
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
        todo_question.insert {
            it[question_id] = f1
            it[program_id] = f2
            it[display_name] = f3
            it[comment] = f4
        }
    }
}
