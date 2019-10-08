package cndjp.qicoo.domain.repository.question_aggr

import cndjp.qicoo.domain.dao.done_question.DoneQuestionRow
import cndjp.qicoo.domain.dao.question_aggr.QuestionAggr
import cndjp.qicoo.domain.dao.question_aggr.QuestionAggrList
import cndjp.qicoo.domain.dao.todo_question.TodoQuestionRow
import cndjp.qicoo.api.QicooError
import com.github.michaelbull.result.Result

interface QuestionAggrRepository {
    fun findAll(per: Int, page: Int, order: String): QuestionAggrList
    fun findById(id: Int): QuestionAggr?
    fun findByIds(ids: List<Int>): Result<QuestionAggrList, QicooError>
    fun insert(comment: String): TodoQuestionRow?
    fun todo2done(id: Int): DoneQuestionRow?
}
