package domain.repository.question

import domain.dao.question_context.QuestionContext
import java.util.*

interface QuestionRepository {
    fun findAll(): List<QuestionContext>
    fun findById(id: UUID): QuestionContext?
}