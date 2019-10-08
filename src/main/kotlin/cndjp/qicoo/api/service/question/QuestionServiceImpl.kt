package cndjp.qicoo.api.service.question

import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetParameter
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetSortParameter
import cndjp.qicoo.domain.dto.question.QuestionDTO
import cndjp.qicoo.domain.dto.question.QuestionListDTO
import cndjp.qicoo.domain.repository.like_count.LikeCountRepository
import cndjp.qicoo.domain.repository.question_aggr.QuestionAggrRepository
import cndjp.qicoo.api.QicooError
import cndjp.qicoo.api.withLog
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.toResultOr
import mu.KotlinLogging
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class QuestionServiceImpl(override val kodein: Kodein) : QuestionService, KodeinAware {
    private val questionAggrRepository: QuestionAggrRepository by instance()
    private val likeCountRepository: LikeCountRepository by instance()
    private val logger = KotlinLogging.logger {}

    override fun getAll(param: QuestionGetParameter): Result<QuestionListDTO, QicooError> =
        when (param.sort) {
            QuestionGetSortParameter.created -> questionAggrRepository.findAll(param.per, param.page, param.order.name)
                .let { findResult ->
                    Ok(QuestionListDTO(
                        // RedisのZRANGEにvalue(question id)でフィルタリングとかするコマンドなかったから全部単発で投げるけどいいよね
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
                    ))
                }
            QuestionGetSortParameter.like ->
                likeCountRepository.findAll(param.per, param.page, param.order.name)
                    .flatMap { redisResult ->
                        questionAggrRepository.findByIds(
                            redisResult.list.map {
                                it.question_id ?: 0
                            }
                        )
                            .flatMap { listFromMySQL ->
                                listFromMySQL.list.map { it.question_id to it }.toMap()
                                    .let { when (redisResult.list.size == it.size) {
                                        true -> Ok(it)
                                        false -> Err(QicooError(cndjp.qicoo.api.QicooErrorReason.MismatchDataStoreFailure.withLog()))
                                    } }
                                    .flatMap { mapFromMysql ->
                                        Ok(QuestionListDTO(
                                            redisResult.list.mapNotNull { redisDAO ->
                                                mapFromMysql[redisDAO.question_id]?.let { mysqlDAO ->
                                                    QuestionDTO(
                                                        redisDAO.question_id ?: 0,
                                                        mysqlDAO.program_name,
                                                        mysqlDAO.event_name,
                                                        mysqlDAO.display_name,
                                                        redisDAO.count ?: 0,
                                                        mysqlDAO.comment,
                                                        mysqlDAO.created,
                                                        mysqlDAO.updated
                                                    )
                                                }
                                            },
                                            redisResult.total
                                        ))
                                    }
                            }
                    }
        }

    override fun createQuestion(comment: String): Result<Unit, QicooError> =
        questionAggrRepository.insert(comment)
            .toResultOr {
                QicooError(cndjp.qicoo.api.QicooErrorReason.CannotCreateEntityFailure.withLog())
            }
            .flatMap {
                likeCountRepository.create(it.question_id)
            }

    override fun incr(questionId: Int) {
        likeCountRepository.incr(questionId)
    }

    override fun answer(questionId: Int): Result<Unit, QicooError> =
        questionAggrRepository.todo2done(questionId)
            .toResultOr { QicooError(cndjp.qicoo.api.QicooErrorReason.CannotCreateEntityFailure.withLog()) }
            .flatMap {
                logger.debug("question id ${it.question_id} from todo to done")
                Ok(Unit)
            }
}
