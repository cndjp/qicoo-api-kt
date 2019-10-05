package domain.repository.question_aggr

import domain.dao.question_aggr.QuestionAggr
import domain.dao.question_aggr.QuestionAggrList
import domain.dao.todo_question.NewTodoQuestion
import java.util.UUID

interface QuestionAggrRepository {
    fun findAll(per: Int, page: Int, order: String): QuestionAggrList
    fun findById(id: UUID): QuestionAggr?
    fun findByIds(ids: List<UUID>): QuestionAggrList
    fun insert(comment: String): NewTodoQuestion?
}
