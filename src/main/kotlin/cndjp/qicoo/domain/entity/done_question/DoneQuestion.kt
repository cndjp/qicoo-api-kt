package domain.entity.done_question

import domain.dao.done_question.done_question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import java.util.*


data class DoneQuestion(
    val question_id: EntityID<UUID>,
    val program_id: EntityID<UUID>,
    val display_name: String,
    val like_count: Int,
    val comment: String
)

fun ResultRow.toEntity(): DoneQuestion =
    DoneQuestion(
        this[done_question.question_id],
        this[done_question.program_id],
        this[done_question.display_name],
        this[done_question.like_count],
        this[done_question.comment]
    )