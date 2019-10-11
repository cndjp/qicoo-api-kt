package test.cndjp.qicoo.domain.repository

import cndjp.qicoo.domain.dao.question_aggr.QuestionAggr
import cndjp.qicoo.domain.dao.question_aggr.QuestionAggrList
import cndjp.qicoo.domain.repository.question_aggr.QuestionAggrRepository
import cndjp.qicoo.api.QicooError
import cndjp.qicoo.domain.dao.question.NewQuestionId
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import test.cndjp.qicoo.domain.repository.support.RepositorySpecSupport

class QuestionAggrRepositoryMock : QuestionAggrRepository {
    private val ss = RepositorySpecSupport

    override fun findAll(per: Int, page: Int, order: String): QuestionAggrList =
        when (Triple(per, page, order)) {
            Triple(2, 1, "desc") -> QuestionAggrList(
                listOf(
                    QuestionAggr(
                        1,
                        ss.e1name,
                        ss.p1name,
                        ss.q1dflg,
                        ss.q1dname,
                        ss.q1comment,
                        ss.q1date,
                        ss.q1date
                    ),
                    QuestionAggr(
                        2,
                        ss.e2name,
                        ss.p2name,
                        ss.q1dflg,
                        ss.q2dname,
                        ss.q2comment,
                        ss.q2date,
                        ss.q2date
                    )
                ),
                5
            )
            else -> TODO()
        }

    override fun findById(id: Int): Result<QuestionAggr, QicooError> =
        Ok(QuestionAggr(
            6,
            ss.e6name,
            ss.p6name,
            ss.q6dflg,
            ss.q6dname,
            ss.q6comment,
            ss.q6date,
            ss.q6date
        ))

    override fun findByIds(ids: List<Int>): Result<QuestionAggrList, QicooError> =
        when (ids) {
            listOf(3, 4, 5) -> Ok(QuestionAggrList(
                listOf(
                    QuestionAggr(
                        3,
                        ss.e3name,
                        ss.p3name,
                        ss.q3dflg,
                        ss.q3dname,
                        ss.q3comment,
                        ss.q3date,
                        ss.q3date
                    ),
                    QuestionAggr(
                        4,
                        ss.e4name,
                        ss.p4name,
                        ss.q4dflg,
                        ss.q4dname,
                        ss.q4comment,
                        ss.q4date,
                        ss.q4date
                    ),
                    QuestionAggr(
                        5,
                        ss.e5name,
                        ss.p5name,
                        ss.q5dflg,
                        ss.q5dname,
                        ss.q5comment,
                        ss.q5date,
                        ss.q5date
                    )
                ),
                3
            ))
            else -> TODO()
        }

    override fun insert(comment: String): Result<NewQuestionId, QicooError> =
        Ok(6)

    override fun todo2done(id: Int): Result<Unit, QicooError> =
        Ok(Unit)
}
