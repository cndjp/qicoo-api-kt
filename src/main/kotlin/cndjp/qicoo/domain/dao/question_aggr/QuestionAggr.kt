package domain.dao.question_aggr

import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.program.program
import domain.model.question.question
import domain.model.todo_question.todo_question
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import utils.toDateTimeJstgForMySQL

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
        created = f6.toDateTimeJstgForMySQL()
        updated = f7.toDateTimeJstgForMySQL()
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
