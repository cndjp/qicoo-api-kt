package domain.repository.todo_question

import domain.dao.todo_question.TodoQuestion
import domain.dao.todo_question.toTodoQuestion
import domain.model.todo_question.todo_question
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TodoQuestionRepositoryImpl: TodoQuestionRepository {
    override fun findAll(): List<TodoQuestion> = transaction { todo_question.selectAll().map{it.toTodoQuestion()} }
    override fun findById(): TodoQuestion? {
        TODO()
    }
}