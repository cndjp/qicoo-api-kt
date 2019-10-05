package api.service.question

import api.http_resource.paramater.question.QuestionGetParameter
import domain.dto.question.QuestionListDTO

interface QuestionService {
    fun getAll(param: QuestionGetParameter): QuestionListDTO
    fun createQuestion(comment: String)
    fun incr(questionId: Int)
}
