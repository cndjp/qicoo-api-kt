package api.service.question

import domain.dto.question.QuestionListDTO

interface QuestionService {
    fun getAll(per: Int, page: Int): QuestionListDTO
    fun create(comment: String): Unit
}