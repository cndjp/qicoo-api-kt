package api.service.question

import domain.dto.question.QuestionDTO
import domain.dto.question.QuestionListDTO
import domain.model.like_count.LikeCountRowKey
import domain.repository.like_count.LikeCountRepository
import domain.repository.question_aggr.QuestionAggrRepository
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import utils.NotfoundEntityException
import utils.RetResult
import java.util.UUID

class QuestionServiceImpl(override val kodein: Kodein): QuestionService, KodeinAware {
    private val questionAggrRepository: QuestionAggrRepository by instance()
    private val likeCountRepository: LikeCountRepository by instance()
    override fun getAll(per: Int, page: Int): QuestionListDTO {
        val result = questionAggrRepository.findAll(per, page)
        return QuestionListDTO(
            result.list.map {
                val likeCount = likeCountRepository.findById(LikeCountRowKey(it.question_id))?.count ?: 0
                QuestionDTO(
                    it.program_name,
                    it.event_name,
                    it.display_name,
                    likeCount,
                    it.comment,
                    it.created,
                    it.updated
                )
            }, result.count
        )
    }

    override fun createQuestion(comment: String) =
        questionAggrRepository.insert(comment)

    override fun incrOrCreateLike(questionId: UUID) {
        likeCountRepository.incr(LikeCountRowKey(questionId))
    }
}