package cndjp.qicoo.domain.dao.question_aggr

import cndjp.qicoo.domain.model.done_question.done_question
import cndjp.qicoo.domain.model.event.event
import cndjp.qicoo.domain.model.program.program
import cndjp.qicoo.domain.model.question.question
import cndjp.qicoo.domain.model.todo_question.todo_question
import cndjp.qicoo.utils.toDateTimeJstForMySQL
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime

class QuestionAggr {
    val question_id: Int
    val event_name: String
    val program_name: String
    val display_name: String
    val comment: String
    val created: DateTime
    val updated: DateTime

    constructor(f1: Int, f2: String, f3: String, f4: String, f5: String, f6: String, f7: String) {
        question_id = f1
        event_name = f2
        program_name = f3
        display_name = f4
        comment = f5
        created = f6.toDateTimeJstForMySQL()
        updated = f7.toDateTimeJstForMySQL()
    }

    constructor(
        f1: Int,
        f2: String,
        f3: String,
        f4: String,
        f5: String,
        f6: DateTime,
        f7: DateTime
    ) {
        question_id = f1
        event_name = f2
        program_name = f3
        display_name = f4
        comment = f5
        created = f6
        updated = f7
    }
}

fun ResultRow.toQuestionAggrFromDone(): QuestionAggr =
    QuestionAggr(
        this[question.id].value,
        this[event.name],
        this[program.name],
        this[done_question.display_name],
        this[done_question.comment],
        this[question.created],
        this[question.updated]
    )

fun ResultRow.toQuestionAggrFromTodo(): QuestionAggr =
    QuestionAggr(
        this[question.id].value,
        this[event.name],
        this[program.name],
        this[todo_question.display_name],
        this[todo_question.comment],
        this[question.created],
        this[question.updated]
    )
