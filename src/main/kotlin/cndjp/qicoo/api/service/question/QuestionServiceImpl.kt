package cndjp.qicoo.api.service.question

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetParameter
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetSortParameter
import cndjp.qicoo.api.withLog
import cndjp.qicoo.domain.dao.like_count.LikeCountValue
import cndjp.qicoo.domain.dto.question.QuestionDTO
import cndjp.qicoo.domain.dto.question.QuestionListDTO
import cndjp.qicoo.domain.repository.like_count.LikeCountRepository
import cndjp.qicoo.domain.repository.question_aggr.QuestionAggrRepository
import cndjp.qicoo.domain.repository.reply.ReplyRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.get
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class QuestionServiceImpl(override val kodein: Kodein) : QuestionService, KodeinAware {
    private val questionAggrRepository: QuestionAggrRepository by instance()
    private val likeCountRepository: LikeCountRepository by instance()
    private val replyRepository: ReplyRepository by instance()

    override fun getAll(param: QuestionGetParameter): Result<QuestionListDTO, QicooError> =
        when (param.sort) {
            QuestionGetSortParameter.created -> questionAggrRepository.findAll(param.per, param.page, param.order.name)
                .let { findResult ->
                    Ok(QuestionListDTO(
                        // RedisのZRANGEにvalue(question id)でフィルタリングとかするコマンドなかったから全部単発で投げるけどいいよね
                        findResult.list.map { dao ->
                            val likeCount = likeCountRepository
                                .findById(dao.question_id).get()?.count ?: 0
                            val replyList = replyRepository.findById(dao.question_id)
                            QuestionDTO(
                                dao.question_id,
                                dao.event_name,
                                dao.program_name,
                                dao.done_flag,
                                dao.display_name,
                                likeCount,
                                dao.comment,
                                dao.created,
                                dao.updated,
                                replyList.list,
                                replyList.total
                            )
                        }, findResult.total
                    ))
                }
            QuestionGetSortParameter.like ->
                likeCountRepository.findAll(param.per, param.page, param.order.name)
                    .flatMap { likeCountResult ->
                        questionAggrRepository.findByIds(
                            likeCountResult.list.map {
                                it.question_id ?: 0
                            }
                        )
                            .flatMap { questionAggrList ->
                                questionAggrList.list.map { it.question_id to it }.toMap()
                                    .let { when (likeCountResult.list.size == it.size) {
                                        true -> Ok(it)
                                        false -> Err(QicooError.MismatchDataStoreFailure.withLog())
                                    } }
                                    .flatMap { questionDAOMap ->
                                        Ok(QuestionListDTO(
                                            likeCountResult.list.mapNotNull { likeCountDAO ->
                                                questionDAOMap[likeCountDAO.question_id]?.let { questionDAO ->
                                                    val replyList = replyRepository.findById(questionDAO.question_id)
                                                    QuestionDTO(
                                                        likeCountDAO.question_id ?: 0,
                                                        questionDAO.event_name,
                                                        questionDAO.program_name,
                                                        questionDAO.done_flag,
                                                        questionDAO.display_name,
                                                        likeCountDAO.count ?: 0,
                                                        questionDAO.comment,
                                                        questionDAO.created,
                                                        questionDAO.updated,
                                                        replyList.list,
                                                        replyList.total
                                                    )
                                                }
                                            },
                                            likeCountResult.total
                                        ))
                                    }
                            }
                    }
        }

    override fun createQuestion(comment: String): Result<QuestionDTO, QicooError> =
        questionAggrRepository.insert(comment)
            .flatMap { questionId ->
                likeCountRepository.create(questionId)
                    .flatMap {
                        questionAggrRepository.findById(questionId)
                            .flatMap { dao ->
                                Ok(QuestionDTO(
                                    dao.question_id,
                                    dao.event_name,
                                    dao.program_name,
                                    dao.done_flag,
                                    dao.display_name,
                                    0,
                                    dao.comment,
                                    dao.created,
                                    dao.updated,
                                    listOf(),
                                    0
                                ))
                            }
                    }
            }

    override fun incrLike(questionId: Int): Result<LikeCountValue, QicooError> =
        likeCountRepository.incr(questionId)

    override fun answer(questionId: Int): Result<Unit, QicooError> =
        questionAggrRepository.todo2done(questionId)

    override fun addReply(questionId: Int, comment: String): Result<QuestionDTO, QicooError> =
        questionAggrRepository.findById(questionId)
            .flatMap { dao ->
                replyRepository.add(dao.question_id, comment)
                    .flatMap {
                        val likeCount = likeCountRepository
                            .findById(dao.question_id).get()?.count ?: 0
                        val replyList = replyRepository.findById(dao.question_id)
                        Ok(QuestionDTO(
                            dao.question_id,
                            dao.event_name,
                            dao.program_name,
                            dao.done_flag,
                            dao.display_name,
                            likeCount,
                            dao.comment,
                            dao.created,
                            dao.updated,
                            replyList.list,
                            replyList.total
                        ))
                    }
            }
}
