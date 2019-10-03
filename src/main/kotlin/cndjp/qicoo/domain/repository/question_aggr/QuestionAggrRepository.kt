package domain.repository.question_aggr

import domain.dao.done_question.NewTodoQuestion
import domain.dao.question_aggr.QuestionAggr
import java.util.*

interface QuestionAggrRepository {
    fun findAll(): List<QuestionAggr>
    fun findById(id: UUID): QuestionAggr?
    fun insert(comment: String): NewTodoQuestion?
}