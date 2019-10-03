package api.service.question

import domain.dto.question.QuestionDTO
import utils.RetResult

interface QuestionService {
    fun getAll(): List<QuestionDTO>
    fun create(comment: String): RetResult
}