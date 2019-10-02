package domain.dao.question_context

import domain.dao.done_question.DoneQuestion
import domain.dao.question.Question
import domain.dao.todo_question.TodoQuestion
import org.jetbrains.exposed.dao.EntityID
import org.joda.time.DateTime
import java.util.*

data class QuestionContext(
    val question_id: EntityID<UUID>,
    val program_id: EntityID<UUID>,
    val display_name: String,
    val like_count: Int,
    val comment: String,
    val created: DateTime,
    val updated: DateTime
)

fun DoneQuestion.factory(mother: Question): QuestionContext = QuestionContext(
    this.question_id,
    this.program_id,
    this.display_name,
    this.like_count,
    this.comment,
    mother.created,
    mother.updated
)

fun TodoQuestion.factory(mother: Question): QuestionContext = QuestionContext(
    this.question_id,
    this.program_id,
    this.display_name,
    this.like_count,
    this.comment,
    mother.created,
    mother.updated
)