package domain.dao.question_aggr

import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.program.program
import domain.model.question.question
import domain.model.todo_question.todo_question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import utils.toDateTimeJstgForMySQL
import java.util.*

//class QuestionAggr {
//    val question_id: EntityID<UUID>
//    val event_name: String
//    val program_name: String
//    val display_name: String
//    val comment: String
//    val created: DateTime
//    val updated: DateTime
//
//    constructor(aggr: DoneQuestionAggr) {
//        this.question_id = aggr.question_id
//        this.event_name = aggr.event_name
//        this.program_name = aggr.program_name
//        this.display_name = aggr.display_name
//        this.comment = aggr.comment
//        this.created = aggr.created
//        this.updated = aggr.updated
//    }
//
//    constructor(aggr: TodoQuestionAggr) {
//        this.question_id = aggr.question_id
//        this.event_name = aggr.event_name
//        this.program_name = aggr.program_name
//        this.display_name = aggr.display_name
//        this.comment = aggr.comment
//        this.created = aggr.created
//        this.updated = aggr.updated
//    }
//}

class QuestionAggr(f1: ByteArray, f2: String, f3: String, f4: String, f5: String, f6: String, f7: String) {
    val question_id: UUID
    val event_name: String
    val program_name: String
    val display_name: String
    val comment: String
    val created: DateTime
    val updated: DateTime

    init {
        question_id = UUID.nameUUIDFromBytes(f1)
        event_name = f2
        program_name = f3
        display_name = f4
        comment = f5
        created = f6.toDateTimeJstgForMySQL()
        updated = f7.toDateTimeJstgForMySQL()
    }
}

data class DoneQuestionAggr (
    val question_id: EntityID<UUID>,
    val event_name: String,
    val program_name: String,
    val display_name: String,
    val comment: String,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toDoneQuestionAggr(): DoneQuestionAggr = DoneQuestionAggr(
    this[question.id],
    this[event.name],
    this[program.name],
    this[done_question.display_name],
    this[done_question.comment],
    this[question.created],
    this[question.updated]
)

data class TodoQuestionAggr (
    val question_id: EntityID<UUID>,
    val event_name: String,
    val program_name: String,
    val display_name: String,
    val comment: String,
    val created: DateTime,
    val updated: DateTime
)

fun ResultRow.toTodoQuestionAggr(): TodoQuestionAggr = TodoQuestionAggr(
    this[question.id],
    this[event.name],
    this[program.name],
    this[todo_question.display_name],
    this[todo_question.comment],
    this[question.created],
    this[question.updated]
)