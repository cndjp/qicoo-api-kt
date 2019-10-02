package domain.repository.question_aggr

import domain.dao.question_aggr.QuestionAggr
import domain.dao.question_aggr.factory
import domain.dao.question_aggr.toDoneQuestionAggr
import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.program.program
import domain.model.question.question
import domain.model.todo_question.todo_question
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import utils.merge
import java.util.*

class QuestionAggrRepositoryImpl: QuestionAggrRepository {
    override fun findAll(): List<QuestionAggr> = transaction {
        val done_aggr = question
            .innerJoin(
                done_question.innerJoin(
                    program.innerJoin(
                        event,
                        { event_id },
                        { id }
                    ),
                    { program_id },
                    { program.id }
                ),
                { done_question.question_id },
                { question.id }
            )
            .slice(question.id, event.name, program.name, done_question.display_name, done_question.like_count, done_question.comment, question.created, question.updated)
            .selectAll().map {it.toDoneQuestionAggr()}
            .map{it.factory()}
        val todo_aggr = question
            .innerJoin(
                todo_question.innerJoin(
                    program.innerJoin(
                        event,
                        { event_id },
                        { id }
                    ),
                    { program_id },
                    { program.id }
                ),
                { todo_question.question_id },
                { question.id }
            )
            .slice(question.id, event.name, program.name, todo_question.display_name, todo_question.like_count, todo_question.comment, question.created, question.updated)
            .selectAll().map {it.toDoneQuestionAggr()}
            .map{it.factory()}

        when (Pair(done_aggr.isNotEmpty(), todo_aggr.isNotEmpty())) {
            Pair(first = true, second = true) -> done_aggr merge todo_aggr
            Pair(first = true, second = false) -> done_aggr
            Pair(first = false, second = true) -> todo_aggr
            else -> listOf()
        }
    }
    override fun findById(id: UUID): QuestionAggr? {
        TODO()
    }
}