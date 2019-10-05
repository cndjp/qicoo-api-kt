package domain.repository.question_aggr

import domain.dao.question_aggr.QuestionAggr
import domain.dao.question_aggr.QuestionAggrList
import domain.dao.todo_question.NewTodoQuestion
import java.util.UUID

interface QuestionAggrRepository {
    fun findAll(per: Int, page: Int, order: String): QuestionAggrList
    fun findById(id: Int): QuestionAggr?
    fun findByIds(ids: List<Int>): QuestionAggrList
    fun insert(comment: String): NewTodoQuestion?
}
