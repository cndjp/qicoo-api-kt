package domain.repository.done_question

import domain.dao.done_question.DoneQuestion
import domain.dao.question.Question

interface DoneQuestionRepository {
    fun findAll(): List<DoneQuestion>
    fun findById(): DoneQuestion?
}