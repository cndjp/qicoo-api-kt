package api.service.question

import domain.dto.program.Program
import domain.dto.question.QuestionDTO
import domain.repository.event.EventRepository
import domain.repository.event.ProgramRepository
import domain.repository.question.QuestionRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.util.*

class QuestionServiceImpl(override val kodein: Kodein): QuestionService, KodeinAware {
    private val questionRepository: QuestionRepository by instance()
    private val programRepository: ProgramRepository by instance()
    private val eventRepository: EventRepository by instance()
    override fun getAll(): List<QuestionDTO> =
        questionRepository.findAll().map {
            val program = programRepository.findById(it.program_id.value)
            val event = program?.let{p: Program ->  p.event_id.value}?.let { eid: UUID -> eventRepository.findById(eid) }
            val eventName = event?.name ?: ""
            QuestionDTO(
                program?.name,
                eventName,
                it.display_name,
                it.like_count,
                it.comment,
                it.created,
                it.updated
            )
        }
}