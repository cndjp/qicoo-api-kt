package api.service.question

import domain.dto.question.QuestionDTO
import domain.repository.question.QuestionRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class QuestionServiceImpl(override val kodein: Kodein): QuestionService, KodeinAware {
    private val questionRepository: QuestionRepository by instance()
    override fun getAll(): QuestionDTO =
        QuestionDTO(questionRepository.findAll())
}