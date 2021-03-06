package test.cndjp.qicoo.api.service

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetParameter
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetSortParameter
import cndjp.qicoo.api.service.question.QuestionService
import cndjp.qicoo.domain.dao.like_count.LikeCountValue
import cndjp.qicoo.domain.dao.reply.Reply
import cndjp.qicoo.domain.dto.question.QuestionDTO
import cndjp.qicoo.domain.dto.question.QuestionListDTO
import cndjp.qicoo.domain.model.reply.ReplyRow
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import test.cndjp.qicoo.infrastructure.support.migration_run.RepositorySpecSupport

class QuestionServiceMock: QuestionService {
    private val ss = RepositorySpecSupport

    override fun getAll(param: QuestionGetParameter): Result<QuestionListDTO, QicooError> =
         when (param.sort) {
             QuestionGetSortParameter.created -> Ok(
                 QuestionListDTO(
                     listOf(
                         QuestionDTO(
                             1,
                             ss.e1name,
                             ss.p1name,
                             ss.q1dflg,
                             ss.q1dname,
                             ss.q1like.toInt(),
                             ss.q1comment,
                             ss.q1date,
                             ss.q1date,
                             listOf(),
                             0
                         ),
                         QuestionDTO(
                             2,
                             ss.e2name,
                             ss.p2name,
                             ss.q2dflg,
                             ss.q2dname,
                             ss.q2like.toInt(),
                             ss.q2comment,
                             ss.q2date,
                             ss.q2date,
                             listOf(),
                             0
                         ),
                         QuestionDTO(
                             3,
                             ss.e3name,
                             ss.p3name,
                             ss.q3dflg,
                             ss.q3dname,
                             ss.q3like.toInt(),
                             ss.q3comment,
                             ss.q3date,
                             ss.q3date,
                             listOf(
                                 Reply(
                                     ReplyRow(
                                         ss.q3reply1date,
                                         ss.q3reply1
                                     )
                                 ),
                                 Reply(
                                     ReplyRow(
                                         ss.q3reply2date,
                                         ss.q3reply2
                                     )
                                 ),
                                 Reply(
                                     ReplyRow(
                                         ss.q3reply3date,
                                         ss.q3reply3
                                     )
                                 )
                             ),
                             3
                         ),
                         QuestionDTO(
                             4,
                             ss.e4name,
                             ss.p4name,
                             ss.q4dflg,
                             ss.q4dname,
                             ss.q4like.toInt(),
                             ss.q4comment,
                             ss.q4date,
                             ss.q4date,
                             listOf(),
                             0
                         ),
                         QuestionDTO(
                             5,
                             ss.e5name,
                             ss.p5name,
                             ss.q5dflg,
                             ss.q5dname,
                             ss.q5like.toInt(),
                             ss.q5comment,
                             ss.q5date,
                             ss.q5date,
                             listOf(),
                             0
                         )
                     ),
                     5
                 )
             )
             QuestionGetSortParameter.like -> Ok(
                 QuestionListDTO(
                     listOf(
                         QuestionDTO(
                             3,
                             ss.e3name,
                             ss.p3name,
                             ss.q3dflg,
                             ss.q3dname,
                             ss.q3like.toInt(),
                             ss.q3comment,
                             ss.q3date,
                             ss.q3date,
                             listOf(
                                 Reply(
                                     ReplyRow(
                                         ss.q3reply1date,
                                         ss.q3reply1
                                     )
                                 ),
                                 Reply(
                                     ReplyRow(
                                         ss.q3reply2date,
                                         ss.q3reply2
                                     )
                                 ),
                                 Reply(
                                     ReplyRow(
                                         ss.q3reply3date,
                                         ss.q3reply3
                                     )
                                 )
                             ),
                             3
                         ),
                         QuestionDTO(
                             4,
                             ss.e4name,
                             ss.p4name,
                             ss.q4dflg,
                             ss.q4dname,
                             ss.q4like.toInt(),
                             ss.q4comment,
                             ss.q4date,
                             ss.q4date,
                             listOf(),
                             0
                         ),
                         QuestionDTO(
                             5,
                             ss.e5name,
                             ss.p5name,
                             ss.q5dflg,
                             ss.q5dname,
                             ss.q5like.toInt(),
                             ss.q5comment,
                             ss.q5date,
                             ss.q5date,
                             listOf(),
                             0
                         ),
                         QuestionDTO(
                             1,
                             ss.e1name,
                             ss.p1name,
                             ss.q1dflg,
                             ss.q1dname,
                             ss.q1like.toInt(),
                             ss.q1comment,
                             ss.q1date,
                             ss.q1date,
                             listOf(),
                             0
                         )
                     ),
                     5
                 )
             )
             else -> TODO()
         }

    override fun createQuestion(comment: String): Result<QuestionDTO, QicooError> = Ok(
        QuestionDTO(
            6,
            ss.e6name,
            ss.p6name,
            ss.q6dflg,
            ss.q6dname,
            0,
            ss.q6comment,
            ss.q6date,
            ss.q6date,
            listOf(),
            0
    ))
    override fun answer(questionId: Int): Result<Unit, QicooError> = Ok(Unit)
    override fun incrLike(questionId: Int): Result<LikeCountValue, QicooError> = Ok(5)
    override fun addReply(questionId: Int, comment: String): Result<QuestionDTO, QicooError> = Ok(
        QuestionDTO(
            6,
            ss.e6name,
            ss.p6name,
            ss.q6dflg,
            ss.q6dname,
            0,
            ss.q6comment,
            ss.q6date,
            ss.q6date,
            listOf(
                Reply(
                    ReplyRow(
                        ss.q6reply1date,
                        ss.q6reply1
                    )
                )
            ),
            1
        )
    )
}