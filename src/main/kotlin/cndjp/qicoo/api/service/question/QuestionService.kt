package api.service.question

import domain.dto.question_aggr.QuestionAggrDTO

interface QuestionService {
    fun getAll(): List<QuestionAggrDTO>
}