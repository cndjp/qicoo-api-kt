package test.cndjp.qicoo.domain.repository

import cndjp.qicoo.domain.dao.done_question.DoneQuestionRow
import cndjp.qicoo.domain.dao.question_aggr.QuestionAggr
import cndjp.qicoo.domain.dao.question_aggr.QuestionAggrList
import cndjp.qicoo.domain.dao.todo_question.TodoQuestionRow
import cndjp.qicoo.domain.repository.question_aggr.QuestionAggrRepository
import cndjp.qicoo.api.QicooError
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
                        ss.q1dname,
                        ss.q1comment,
                        ss.q1date,
                        ss.q1date
                    ),
                    QuestionAggr(
                        2,
                        ss.e2name,
                        ss.p2name,
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

    override fun findById(id: Int): QuestionAggr? {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun findByIds(ids: List<Int>): Result<QuestionAggrList, QicooError> =
        when (ids) {
            listOf(3, 4, 5) -> Ok(QuestionAggrList(
                listOf(
                    QuestionAggr(
                        3,
                        ss.e3name,
                        ss.p3name,
                        ss.q3dname,
                        ss.q3comment,
                        ss.q3date,
                        ss.q3date
                    ),
                    QuestionAggr(
                        4,
                        ss.e4name,
                        ss.p4name,
                        ss.q4dname,
                        ss.q4comment,
                        ss.q4date,
                        ss.q4date
                    ),
                    QuestionAggr(
                        5,
                        ss.e5name,
                        ss.p5name,
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

    override fun insert(comment: String): TodoQuestionRow? =
        TodoQuestionRow(
            question_id = 6,
            program_id = 6,
            display_name = ss.p6name,
            comment = ss.q6comment
        )

    override fun todo2done(id: Int): DoneQuestionRow? =
        DoneQuestionRow(
            question_id = 6,
            program_id = 6,
            display_name = ss.p6name,
            comment = ss.q6comment
        )
}
