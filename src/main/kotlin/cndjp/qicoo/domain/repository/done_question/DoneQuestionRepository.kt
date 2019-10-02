package domain.repository.done_question

import domain.dao.question_context.QuestionContext

interface DoneQuestionRepository {
    fun findAll(): List<QuestionContext>
    fun findById(): QuestionContext?
}