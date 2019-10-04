package domain.repository.question_aggr

import domain.dao.done_question.NewTodoQuestion
import domain.dao.question_aggr.QuestionAggr
import java.util.*

interface QuestionAggrRepository {
    fun findAll(per: Int, page: Int): Pair<List<QuestionAggr>, Int>
    fun findById(id: UUID): QuestionAggr?
    fun insert(comment: String): Unit
}