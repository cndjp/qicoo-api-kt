package api.service.question

import domain.dto.question_aggr.QuestionListDTO
import domain.repository.like_count.LikeCountRepository
import domain.repository.question_aggr.QuestionAggrRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class QuestionServiceImpl(override val kodein: Kodein): QuestionService, KodeinAware {
    private val questionAggrRepository: QuestionAggrRepository by instance()
    private val likeCountRepository: LikeCountRepository by instance()
    override fun getAll(): List<QuestionListDTO> =
        questionAggrRepository.findAll().map {
            val likeCount = likeCountRepository.findById(it.question_id.value)?.count?:0
            QuestionListDTO(
                it.program_name,
                it.event_name,
                it.display_name,
                likeCount,
                it.comment,
                it.created,
                it.updated
            )
        }
}