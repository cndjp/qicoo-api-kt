package domain.repository.question_aggr

import domain.dao.done_question.NewDoneQuestion
import domain.dao.question_aggr.QuestionAggr
import domain.dao.question_aggr.QuestionAggrList
import domain.dao.todo_question.NewTodoQuestion

interface QuestionAggrRepository {
    fun findAll(per: Int, page: Int, order: String): QuestionAggrList
    fun findById(id: Int): QuestionAggr?
    fun findByIds(ids: List<Int>): QuestionAggrList
    fun insert(comment: String): NewTodoQuestion?
    fun todo2done(id: Int): NewDoneQuestion?
}
