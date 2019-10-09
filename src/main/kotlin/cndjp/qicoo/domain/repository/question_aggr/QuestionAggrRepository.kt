package cndjp.qicoo.domain.repository.question_aggr

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.domain.dao.question.NewQuestionResult
import cndjp.qicoo.domain.dao.question_aggr.QuestionAggr
import cndjp.qicoo.domain.dao.question_aggr.QuestionAggrList
import com.github.michaelbull.result.Result

interface QuestionAggrRepository {
    fun findAll(per: Int, page: Int, order: String): QuestionAggrList
    fun findById(id: Int): QuestionAggr?
    fun findByIds(ids: List<Int>): Result<QuestionAggrList, QicooError>
    fun insert(comment: String): Result<NewQuestionResult, QicooError>
    fun todo2done(id: Int): Result<Unit, QicooError>
}
