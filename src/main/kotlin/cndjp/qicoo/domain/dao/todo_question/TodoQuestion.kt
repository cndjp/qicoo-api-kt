package domain.dao.todo_question

import domain.model.todo_question.todo_question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow

data class TodoQuestion(
    val question_id: EntityID<Int>,
    val program_id: EntityID<Int>,
    val display_name: String,
    val comment: String
)

fun ResultRow.toTodoQuestion(): TodoQuestion =
    TodoQuestion(
        this[todo_question.question_id],
        this[todo_question.program_id],
        this[todo_question.display_name],
        this[todo_question.comment]
    )
