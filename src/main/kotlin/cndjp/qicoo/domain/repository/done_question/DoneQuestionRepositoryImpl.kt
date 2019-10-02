package domain.repository.done_question

import domain.dao.done_question.DoneQuestion
import domain.dao.done_question.toDoneQuestion
import domain.model.done_question.done_question
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class DoneQuestionRepositoryImpl: DoneQuestionRepository {
    override fun findAll(): List<DoneQuestion> = transaction { done_question.selectAll().map{it.toDoneQuestion()} }
    override fun findById(): DoneQuestion? {
        TODO()
    }
}