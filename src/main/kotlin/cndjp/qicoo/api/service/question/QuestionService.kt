package api.service.question

import domain.dto.question.QuestionListDTO
import java.util.UUID

interface QuestionService {
    fun getAll(per: Int, page: Int): QuestionListDTO
    fun createQuestion(comment: String)
    fun incrOrCreateLike(questionId: UUID)
}
