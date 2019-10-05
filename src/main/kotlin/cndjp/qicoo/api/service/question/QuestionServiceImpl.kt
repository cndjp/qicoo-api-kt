package api.service.question

import api.http_resource.paramater.question.QuestionGetParameter
import api.http_resource.paramater.question.QuestionGetSortParameter
import domain.dto.question.QuestionDTO
import domain.dto.question.QuestionListDTO
import domain.repository.like_count.LikeCountRepository
import domain.repository.question_aggr.QuestionAggrRepository
import java.util.UUID
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import utils.zeroUUID

class QuestionServiceImpl(override val kodein: Kodein) : QuestionService, KodeinAware {
    private val questionAggrRepository: QuestionAggrRepository by instance()
    private val likeCountRepository: LikeCountRepository by instance()

    override fun getAll(param: QuestionGetParameter): QuestionListDTO =
        when (param.sort) {
            QuestionGetSortParameter.created -> questionAggrRepository.findAll(param.per, param.page, param.order.name)
                .let { findResult ->
                    QuestionListDTO(
                        findResult.list.map { dao ->
                            val likeCount = likeCountRepository
                                .findById(dao.question_id)?.count ?: 0
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
                        }, findResult.total
                    )
                }
            QuestionGetSortParameter.like ->
                likeCountRepository.findAll()
                    .let { findResult ->
                        QuestionListDTO(
                        findResult.list.zip(questionAggrRepository.findByIds(
                            findResult.list
                                .map{
                                    it.question_id?: zeroUUID
                                }).list)
                            .map{ dao ->
                                QuestionDTO(
                                    dao.second.question_id,
                                    dao.second.program_name,
                                    dao.second.event_name,
                                    dao.second.display_name,
                                    dao.first.count ?: 0,
                                    dao.second.comment,
                                    dao.second.created,
                                    dao.second.updated
                                )
                            },
                            findResult.total
                        )
                    }
        }

    override fun createQuestion(comment: String) {
        val created = questionAggrRepository.insert(comment)
        created?.question_id?.let {likeCountRepository.create(it.value)}
    }
    override fun incr(questionId: UUID) {
        likeCountRepository.incr(questionId)
    }
}
