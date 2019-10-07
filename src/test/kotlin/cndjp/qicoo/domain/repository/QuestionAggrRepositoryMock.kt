package domain.repository

import domain.dao.done_question.DoneQuestionRow
import domain.dao.question_aggr.QuestionAggr
import domain.dao.question_aggr.QuestionAggrList
import domain.dao.todo_question.TodoQuestionRow
import domain.repository.question_aggr.QuestionAggrRepository
import utils.getNowDateTimeJst

class QuestionAggrRepositoryMock : QuestionAggrRepository {
    val now = getNowDateTimeJst()
    val e1name = "event_name1"
    val e2name = "event_name2"
    val e3name = "event_name3"
    val e4name = "event_name4"
    val e5name = "event_name5"
    val e6name = "event_name6"
    val p1name = "program_name1"
    val p2name = "program_name2"
    val p3name = "program_name3"
    val p4name = "program_name4"
    val p5name = "program_name5"
    val p6name = "program_name6"
    val q1dname = "nyan"
    val q1comment = "what is qicoo"
    val q1date = "2019-09-01 19:00:00.00000000"
    val q2date = "2019-08-01 19:00:00.00000000"
    val q3date = "2019-07-01 19:00:00.00000000"
    val q4date = "2019-06-01 19:00:00.00000000"
    val q5date = "2019-05-01 19:00:00.00000000"
    val q6date = "2019-04-01 19:00:00.00000000"
    val q2dname = "hyon"
    val q2comment = "what is mayo"
    val q3dname = "poe"
    val q3comment = "what is poe"
    val q4dname = "puyo"
    val q4comment = "what is myas"
    val q5dname = "mere"
    val q5comment = "what is hugai"
    val q6dname = "fera"
    val q6comment = "what is lapsc"

    override fun findAll(per: Int, page: Int, order: String): QuestionAggrList =
        when (Triple(per, page, order)) {
            Triple(2, 1, "desc") -> QuestionAggrList(
                listOf(
                    QuestionAggr(
                        1,
                        e1name,
                        p1name,
                        q1dname,
                        q1comment,
                        q1date,
                        q1date
                    ),
                    QuestionAggr(
                        2,
                        e2name,
                        p2name,
                        q2dname,
                        q2comment,
                        q2date,
                        q2date
                    )
                ),
                2
            )
            else -> TODO()
        }


    override fun findById(id: Int): QuestionAggr? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findByIds(ids: List<Int>): QuestionAggrList =
        when (ids) {
            listOf(3, 4, 5) ->QuestionAggrList(
                listOf(
                    QuestionAggr(
                        3,
                        e3name,
                        p3name,
                        q3dname,
                        q3comment,
                        q3date,
                        q3date
                    ),
                    QuestionAggr(
                        4,
                        e4name,
                        p4name,
                        q4dname,
                        q4comment,
                        q4date,
                        q4date
                    ),
                    QuestionAggr(
                        5,
                        e5name,
                        p5name,
                        q5dname,
                        q5comment,
                        q5date,
                        q5date
                    )
                ),
                3
            )
            else -> TODO()
        }


    override fun insert(comment: String): TodoQuestionRow? =
        TodoQuestionRow(
            question_id = 6,
            program_id = 6,
            display_name = p6name,
            comment = q6comment
        )

    override fun todo2done(id: Int): DoneQuestionRow? =
        DoneQuestionRow(
            question_id = 6,
            program_id = 6,
            display_name = p6name,
            comment = q6comment
        )
}