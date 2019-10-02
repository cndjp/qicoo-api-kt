package domain.dao.todo_question

import domain.model.todo_question.todo_question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import java.util.*


data class TodoQuestion(
    val question_id: EntityID<UUID>,
    val program_id: EntityID<UUID>,
    val display_name: String,
    val like_count: Int,
    val comment: String
)

fun ResultRow.toTodoQuestion(): TodoQuestion =
    TodoQuestion(
        this[todo_question.question_id],
        this[todo_question.program_id],
        this[todo_question.display_name],
        this[todo_question.like_count],
        this[todo_question.comment]
    )