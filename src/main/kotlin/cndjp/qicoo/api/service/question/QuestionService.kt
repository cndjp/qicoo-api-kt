package api.service.question

import domain.dto.question_aggr.QuestionListDTO

interface QuestionService {
    fun getAll(): List<QuestionListDTO>
}