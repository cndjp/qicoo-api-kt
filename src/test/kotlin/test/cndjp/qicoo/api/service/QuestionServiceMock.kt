package test.cndjp.qicoo.api.service

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetParameter
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetSortParameter
import cndjp.qicoo.api.service.question.QuestionService
import cndjp.qicoo.domain.dao.like_count.LikeCountValue
import cndjp.qicoo.domain.dto.question.QuestionDTO
import cndjp.qicoo.domain.dto.question.QuestionListDTO
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import test.cndjp.qicoo.domain.repository.support.RepositorySpecSupport

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
                             ss.q1date
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
                             ss.q2date
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
                             ss.q3date
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
                             ss.q4date
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
                             ss.q5date
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
                             ss.q3date
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
                             ss.q4date
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
                             ss.q5date
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
                             ss.q1date
                         )
                     ),
                     5
                 )
             )
             else -> TODO()
         }

    override fun createQuestion(comment: String): Result<Unit, QicooError> = Ok(Unit)
    override fun answer(questionId: Int): Result<Unit, QicooError> = Ok(Unit)
    override fun incr(questionId: Int): Result<LikeCountValue, QicooError> = Ok(5)
}