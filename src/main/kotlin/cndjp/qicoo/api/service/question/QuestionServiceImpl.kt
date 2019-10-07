package api.service.question

import api.http_resource.paramater.question.QuestionGetParameter
import api.http_resource.paramater.question.QuestionGetSortParameter
import domain.dto.question.QuestionDTO
import domain.dto.question.QuestionListDTO
import domain.repository.like_count.LikeCountRepository
import domain.repository.question_aggr.QuestionAggrRepository
import mu.KotlinLogging
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import utils.EntityResult
import utils.checkCreate
import utils.checkNull

class QuestionServiceImpl(override val kodein: Kodein) : QuestionService, KodeinAware {
    private val questionAggrRepository: QuestionAggrRepository by instance()
    private val likeCountRepository: LikeCountRepository by instance()
    private val logger = KotlinLogging.logger {}

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
                likeCountRepository.findAll(param.per, param.page, param.order.name)
                    .let { findResult ->
                        val mapFromMysql = questionAggrRepository.findByIds(
                            findResult.list.map {
                                it.question_id ?: 0
                            }
                        ).list.map{it.question_id to it }.toMap()

                        if (findResult.list.size != mapFromMysql.size) {
                            // 普通起きないと思うが、これが起きた時は大概mysqlとredisの間に不整合がある。
                            logger.error("incorrect value between redis and mysql")
                            return QuestionListDTO(
                                listOf(),
                                0
                            )
                        }

                        QuestionListDTO(
                            findResult.list.mapNotNull { dao ->
                                mapFromMysql[dao.question_id]?.let {
                                    QuestionDTO(
                                        dao.question_id ?: 0,
                                        it.program_name,
                                        it.event_name,
                                        it.display_name,
                                        dao.count ?: 0,
                                        it.comment,
                                        it.created,
                                        it.updated
                                    )
                                }
                            },
                            findResult.total
                        )
                    }
        }

    override fun createQuestion(comment: String): EntityResult {
        val created = questionAggrRepository.insert(comment)
        created?.question_id?.let { return likeCountRepository.create(it).checkCreate() }

        return EntityResult.NotFoundEntityFailure
    }

    override fun incr(questionId: Int): EntityResult =
        likeCountRepository.incr(questionId).checkCreate()


    override fun answer(questionId: Int): EntityResult =
        questionAggrRepository.todo2done(questionId).checkNull()

}
