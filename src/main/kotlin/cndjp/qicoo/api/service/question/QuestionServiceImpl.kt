package api.service.question

import domain.dto.question.QuestionDTO
import domain.dto.question.QuestionListDTO
import domain.model.like_count.LikeCountRowKey
import domain.repository.like_count.LikeCountRepository
import domain.repository.question_aggr.QuestionAggrRepository
import java.util.UUID
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class QuestionServiceImpl(override val kodein: Kodein) : QuestionService, KodeinAware {
    private val questionAggrRepository: QuestionAggrRepository by instance()
    private val likeCountRepository: LikeCountRepository by instance()

    override fun getAll(per: Int, page: Int): QuestionListDTO =
            questionAggrRepository.findAll(per, page)
                .let { findResult ->
                    QuestionListDTO(
                        findResult.list.map { dao ->
                            val likeCount = likeCountRepository
                                .findById(LikeCountRowKey(dao.question_id))?.count ?: 0
                            QuestionDTO(
                                dao.question_id,
                                dao.program_name,
                                dao.event_name,
                                dao.display_name,
                                likeCount,
                                dao.comment,
                                dao.created,
                                dao.updated
                            )
                        }, findResult.count)
                }

    override fun createQuestion(comment: String) =
        questionAggrRepository.insert(comment)

    override fun incrOrCreateLike(questionId: UUID) {
        likeCountRepository.incr(LikeCountRowKey(questionId))
    }
}
