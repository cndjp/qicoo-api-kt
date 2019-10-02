package domain.repository.todo_question

import domain.dao.todo_question.TodoQuestion

interface TodoQuestionRepository {
    fun findAll(): List<TodoQuestion>
    fun findById(): TodoQuestion?
}