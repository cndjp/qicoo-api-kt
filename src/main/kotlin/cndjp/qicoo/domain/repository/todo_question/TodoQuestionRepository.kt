package domain.repository.todo_question

import domain.dao.question_context.QuestionContext
import domain.dao.todo_question.TodoQuestion

interface TodoQuestionRepository {
    fun findAll(): List<QuestionContext>
    fun findById(): QuestionContext?
}