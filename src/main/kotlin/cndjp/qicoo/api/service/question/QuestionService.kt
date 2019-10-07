package cndjp.qicoo.api.service.question

import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetParameter
import cndjp.qicoo.domain.dto.question.QuestionListDTO
import cndjp.qicoo.utils.EntityResult

interface QuestionService {
    fun getAll(param: QuestionGetParameter): QuestionListDTO
    fun createQuestion(comment: String): EntityResult
    fun incr(questionId: Int): EntityResult
    fun answer(questionId: Int): EntityResult
}
