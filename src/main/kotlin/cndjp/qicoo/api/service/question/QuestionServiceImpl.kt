package api.service.question

import api.http_resource.paramater.question.QuestionGetOrderParameter
import api.http_resource.paramater.question.QuestionGetParameter
import api.http_resource.paramater.question.QuestionGetSortParameter
import domain.dao.question.Question
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

    override fun getAll(param: QuestionGetParameter): QuestionListDTO =
            questionAggrRepository.findAll(param.per, param.page, param.order.name)
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
                        }
                            .also { list ->
                                if (param.sort == QuestionGetSortParameter.like) {
                                    when (param.order) {
                                        QuestionGetOrderParameter.asc -> list.sortedBy { dto -> dto.like_count }
                                        else -> list.sortedByDescending { dto -> dto.like_count }
                                    }
                                }
                            },
                        findResult.count)
                }

    override fun createQuestion(comment: String) =
        questionAggrRepository.insert(comment)

    override fun incrOrCreateLike(questionId: UUID) {
        likeCountRepository.incr(LikeCountRowKey(questionId))
    }
}
