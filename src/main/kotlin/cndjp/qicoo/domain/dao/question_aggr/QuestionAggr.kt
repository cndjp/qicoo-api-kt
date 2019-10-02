package domain.dao.question_aggr

import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.program.program
import domain.model.question.question
import domain.model.todo_question.todo_question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

data class QuestionAggr (
    val question_id: EntityID<UUID>,
    val event_name: String,
    val program_name: String,
    val display_name: String,
    val like_count: Int,
    val comment: String,
    val created: DateTime,
    val updated: DateTime
)

fun DoneQuestionAggr.factory(): QuestionAggr = QuestionAggr(
    this.question_id,
    this.event_name,
    this.program_name,
    this.display_name,
    this.like_count,
    this.comment,
    this.created,
    this.updated
)

fun TodoQuestionAggr.factory(): QuestionAggr = QuestionAggr(
    this.question_id,
    this.event_name,
    this.program_name,
    this.display_name,
    this.like_count,
    this.comment,
    this.created,
    this.updated
)

data class DoneQuestionAggr (
    val question_id: EntityID<UUID>,
    val event_name: String,
    val program_name: String,
    val display_name: String,
    val like_count: Int,
    val comment: String,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toDoneQuestionAggr(): DoneQuestionAggr = DoneQuestionAggr(
    this[question.id],
    this[event.name],
    this[program.name],
    this[done_question.display_name],
    this[done_question.like_count],
    this[done_question.comment],
    this[question.created],
    this[question.updated]
)

data class TodoQuestionAggr (
    val question_id: EntityID<UUID>,
    val event_name: String,
    val program_name: String,
    val display_name: String,
    val like_count: Int,
    val comment: String,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toTodoQuestionAggr(): TodoQuestionAggr = TodoQuestionAggr(
    this[question.id],
    this[event.name],
    this[program.name],
    this[todo_question.display_name],
    this[todo_question.like_count],
    this[todo_question.comment],
    this[question.created],
    this[question.updated]
)