package domain.repository.question_aggr

import domain.dao.done_question.NewTodoQuestion
import domain.dao.question_aggr.QuestionAggr
import domain.dao.question_aggr.QuestionAggrList
import java.util.*

interface QuestionAggrRepository {
    fun findAll(per: Int, page: Int): QuestionAggrList
    fun findById(id: UUID): QuestionAggr?
    fun insert(comment: String): Unit
}