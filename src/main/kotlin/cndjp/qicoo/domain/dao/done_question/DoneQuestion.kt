package domain.dao.done_question

import domain.model.done_question.done_question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow

data class DoneQuestion(
    val question_id: EntityID<Int>,
    val program_id: EntityID<Int>,
    val display_name: String,
    val comment: String
)

fun ResultRow.toDoneQuestion(): DoneQuestion =
    DoneQuestion(
        this[done_question.question_id],
        this[done_question.program_id],
        this[done_question.display_name],
        this[done_question.comment]
    )
