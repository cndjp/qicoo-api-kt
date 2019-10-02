package domain.repository.question_aggr

import domain.dao.question_aggr.QuestionAggr
import domain.dao.question_context.QuestionContext
import java.util.*

interface QuestionAggrRepository {
    fun findAll(): List<QuestionAggr>
    fun findById(id: UUID): QuestionAggr?
}