package domain.repository.question_aggr

import domain.dao.done_question.NewTodoQuestion
import domain.dao.program.toProgram
import domain.dao.program.unknownProgram
import domain.dao.question.NewQuestion
import domain.dao.question_aggr.QuestionAggr
import domain.dao.question_aggr.toDoneQuestionAggr
import domain.dao.question_aggr.toTodoQuestionAggr
import domain.dao.todo_question.TodoQuestion
import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.program.program
import domain.model.question.question
import domain.model.todo_question.todo_question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import utils.getNowDateTimeJst
import utils.merge
import utils.zeroUUID
import java.util.*

// ExposedにUNIONとかWITHないからkotlinで行結合する。
// パフォーマンス悪かったら生SQLでやるしかないけど...
class QuestionAggrRepositoryImpl: QuestionAggrRepository {
    override fun findAll(): List<QuestionAggr> = transaction {
        val done_aggr = question
            .innerJoin(
                otherTable = done_question.innerJoin(
                    otherTable = program.innerJoin(
                        otherTable = event,
                        otherColumn = { program.event_id },
                        onColumn = { event.id }
                    ),
                    otherColumn = { done_question.program_id },
                    onColumn = { program.id }
                ),
                otherColumn = { done_question.question_id },
                onColumn = { id }
            )
            .slice(question.id, event.name, program.name, done_question.display_name, done_question.comment, question.created, question.updated)
            .selectAll().map {QuestionAggr(it.toDoneQuestionAggr())}
        val todo_aggr = question
            .innerJoin(
                otherTable = todo_question.innerJoin(
                    otherTable = program.innerJoin(
                        otherTable = event,
                        otherColumn = { program.event_id },
                        onColumn = { event.id }
                    ),
                    otherColumn = { todo_question.program_id },
                    onColumn = { program.id }
                ),
                otherColumn = { todo_question.question_id },
                onColumn = { id }
            )
            .slice(question.id, event.name, program.name, todo_question.display_name, todo_question.comment, question.created, question.updated)
            .selectAll().map {QuestionAggr(it.toTodoQuestionAggr())}

        when (Pair(done_aggr.isNotEmpty(), todo_aggr.isNotEmpty())) {
            Pair(first = true, second = true) -> listOf(done_aggr, todo_aggr).flatten()
            Pair(first = true, second = false) -> done_aggr
            Pair(first = false, second = true) -> todo_aggr
            else -> listOf()
        }
    }
    override fun findById(id: UUID): QuestionAggr? {
        TODO()
    }
    override fun insert(comment: String): NewTodoQuestion? = transaction {
        val now = getNowDateTimeJst()
        val nowProgram = program.select{
            (program.start_at lessEq now) and (program.end_at greaterEq now)
        }.firstOrNull()
        val question = NewQuestion.new {
            created = now
            updated = now
        }
        NewTodoQuestion(
            question_id = question.id,
            program_id = nowProgram?.toProgram()?.id ?: unknownProgram.id,
            display_name = "", // TODO
            comment = comment
        )
    }
}