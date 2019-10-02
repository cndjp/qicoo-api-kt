package api.service.question

import domain.dto.question_aggr.QuestionAggrDTO
import domain.repository.question_aggr.QuestionAggrRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class QuestionServiceImpl(override val kodein: Kodein): QuestionService, KodeinAware {
    private val questionAggrRepository: QuestionAggrRepository by instance()
    override fun getAll(): List<QuestionAggrDTO> =
        questionAggrRepository.findAll().map {
            QuestionAggrDTO(
                it.program_name,
                it.event_name,
                it.display_name,
                it.like_count,
                it.comment,
                it.created,
                it.updated
            )
        }
}