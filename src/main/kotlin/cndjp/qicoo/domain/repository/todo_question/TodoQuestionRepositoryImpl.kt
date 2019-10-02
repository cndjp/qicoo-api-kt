package domain.repository.todo_question

import domain.dao.question.toQuestion
import domain.dao.question_context.QuestionContext
import domain.dao.question_context.factory
import domain.dao.todo_question.TodoQuestion
import domain.dao.todo_question.toTodoQuestion
import domain.model.question.question
import domain.model.todo_question.todo_question
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TodoQuestionRepositoryImpl: TodoQuestionRepository {
    override fun findAll(): List<QuestionContext> = transaction {
        todo_question.selectAll().map {
            it.toTodoQuestion()
        }.map {
            it.factory(question.select { question.id eq it.question_id }.map { q -> q.toQuestion() }.first())
        }
    }
        override fun findById(): QuestionContext? {
        TODO()
    }
}