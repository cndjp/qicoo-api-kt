package cndjp.qicoo.api.service.question

import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetParameter
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetSortParameter
import cndjp.qicoo.domain.dto.question.QuestionDTO
import cndjp.qicoo.domain.dto.question.QuestionListDTO
import cndjp.qicoo.domain.repository.like_count.LikeCountRepository
import cndjp.qicoo.domain.repository.question_aggr.QuestionAggrRepository
import cndjp.qicoo.utils.QicooError
import cndjp.qicoo.utils.QicooErrorReason
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
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
                    .let { findResult ->
                        questionAggrRepository.findByIds(
                            findResult.list.map {
                                it.question_id ?: 0
                            }
                        )
                            .andThen {
                                it.list.map { it.question_id to it }.toMap()
                                    .let { when (findResult.list.size == it.size) {
                                        true -> Ok(it)
                                        false -> Err(QicooError(QicooErrorReason.MismatchDataStoreFailure))
                                    } }
                                    .andThen { mapFromMysql ->
                                        Ok(QuestionListDTO(
                                            findResult.list.mapNotNull { redisDAO ->
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
                                            findResult.total
                                        ))
                                    }
                            }
                            .mapError {
                                it
                            }
                    }
        }

    override fun createQuestion(comment: String): Result<Unit, QicooError> =
        questionAggrRepository.insert(comment)
            .toResultOr {
                QicooError(QicooErrorReason.CannotCreateEntityFailure)
            }
            .andThen {
                likeCountRepository.create(it.question_id)
            }

    override fun incr(questionId: Int) {
        likeCountRepository.incr(questionId)
    }

    override fun answer(questionId: Int): Result<Unit, QicooError> =
        questionAggrRepository.todo2done(questionId)
            .toResultOr { QicooError(QicooErrorReason.CannotCreateEntityFailure) }
            .andThen {
                logger.debug("question id ${it.question_id} from todo to done")
                Ok(Unit)
            }
}
