package api.service.question

import domain.dto.question.QuestionDTO

interface QuestionService {
    fun getAll(): QuestionDTO
}