package domain.repository.question

import domain.dao.done_question.toDoneQuestion
import domain.dao.question.toQuestion
import domain.dao.question_context.QuestionContext
import domain.dao.question_context.factory
import domain.dao.todo_question.toTodoQuestion
import domain.model.done_question.done_question
import domain.model.question.question
import domain.model.todo_question.todo_question
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import utils.merge
import java.util.*

class QuestionRepositoryImpl: QuestionRepository {
    override fun findAll(): List<QuestionContext> = TODO()
    override fun findById(id: UUID): QuestionContext? {
        TODO()
    }
}