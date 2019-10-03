package api.service.question

import domain.dto.question_aggr.QuestionDTO

interface QuestionService {
    fun getAll(): List<QuestionDTO>
}