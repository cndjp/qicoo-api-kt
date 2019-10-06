package api.service.question

import api.http_resource.paramater.question.QuestionGetParameter
import domain.dto.question.QuestionListDTO
import utils.EntityResult

interface QuestionService {
    fun getAll(param: QuestionGetParameter): QuestionListDTO
    fun createQuestion(comment: String): EntityResult
    fun incr(questionId: Int): EntityResult
    fun answer(questionId: Int): EntityResult
}
