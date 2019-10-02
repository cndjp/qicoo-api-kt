package domain.repository.done_question

import domain.dao.done_question.DoneQuestion
import domain.dao.done_question.toDoneQuestion
import domain.dao.question.toQuestion
import domain.dao.question_context.QuestionContext
import domain.dao.question_context.factory
import domain.dao.todo_question.toTodoQuestion
import domain.model.done_question.done_question
import domain.model.question.question
import domain.model.todo_question.todo_question
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class DoneQuestionRepositoryImpl: DoneQuestionRepository {
    override fun findAll(): List<QuestionContext> = transaction { done_question.selectAll().map {
            it.toDoneQuestion()
        }.map{
            it.factory(question.select { question.id eq it.question_id }.map{ q -> q.toQuestion() }.first())
        }
    }
    override fun findById(): QuestionContext? {
        TODO()
    }
}