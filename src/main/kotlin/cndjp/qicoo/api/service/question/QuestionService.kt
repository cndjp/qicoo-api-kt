package cndjp.qicoo.api.service.question

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetParameter
import cndjp.qicoo.domain.dto.question.QuestionListDTO
import com.github.michaelbull.result.Result

interface QuestionService {
    fun getAll(param: QuestionGetParameter): Result<QuestionListDTO, QicooError>
    fun createQuestion(comment: String): Result<Unit, QicooError>
    fun incr(questionId: Int): Result<Unit, QicooError>
    fun answer(questionId: Int): Result<Unit, QicooError>
}
