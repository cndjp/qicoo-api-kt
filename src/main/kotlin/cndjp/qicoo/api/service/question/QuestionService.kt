package api.service.question

import domain.dto.question.QuestionDTO
import utils.RetResult

interface QuestionService {
    fun getAll(per: Int, page: Int): Pair<List<QuestionDTO>, Int>
    fun create(comment: String): Unit
}