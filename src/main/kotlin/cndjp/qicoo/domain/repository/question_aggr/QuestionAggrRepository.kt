package domain.repository.question_aggr

import domain.dao.question_aggr.QuestionAggr
import domain.dao.question_aggr.QuestionAggrList
import java.util.UUID

interface QuestionAggrRepository {
    fun findAll(per: Int, page: Int, isSort: Boolean, order: String): QuestionAggrList
    fun findById(id: UUID): QuestionAggr?
    fun insert(comment: String): Unit
}
