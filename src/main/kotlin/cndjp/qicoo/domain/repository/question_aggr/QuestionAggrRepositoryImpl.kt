package domain.repository.question_aggr

import domain.dao.done_question.NewTodoQuestion
import domain.dao.program.toProgram
import domain.dao.program.unknownProgram
import domain.dao.question.NewQuestion
import domain.dao.question_aggr.QuestionAggr
import domain.dao.question_aggr.toDoneQuestionAggr
import domain.dao.question_aggr.toTodoQuestionAggr
import domain.dao.todo_question.TodoQuestion
import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.program.program
import domain.model.question.question
import domain.model.todo_question.todo_question
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import utils.execAndMap
import utils.getNowDateTimeJst
import utils.merge
import utils.zeroUUID
import java.util.*

class QuestionAggrRepositoryImpl: QuestionAggrRepository {
    // ExposedにUNIONとかWITHないから生SQL使う。
    // パフォーマンス悪かったらもうjooqでやるしかないけど...
    override fun findAll(per: Int, page: Int): Pair<List<QuestionAggr>, Int> = transaction {
        val dq = done_question.alias("join_question")
        val tq = todo_question.alias("join_question")
        val done_aggr_sql = question
            .innerJoin(
                otherTable = dq.innerJoin(
                    otherTable = program.innerJoin(
                        otherTable = event,
                        otherColumn = { program.event_id },
                        onColumn = { event.id }
                    ),
                    otherColumn = { dq[done_question.program_id] },
                    onColumn = { program.id }
                ),
                otherColumn = { dq[done_question.question_id] },
                onColumn = { id }
            )
            .slice(question.id, event.name.alias("event_name"), program.name.alias("program_name"), dq[done_question.display_name], dq[done_question.comment], question.created, question.updated)
            .selectAll()
            .prepareSQL(QueryBuilder(false))
        val todo_aggr_sql = question
            .innerJoin(
                otherTable = tq.innerJoin(
                    otherTable = program.innerJoin(
                        otherTable = event,
                        otherColumn = { program.event_id },
                        onColumn = { event.id }
                    ),
                    otherColumn = { tq[todo_question.program_id] },
                    onColumn = { program.id }
                ),
                otherColumn = { tq[todo_question.question_id] },
                onColumn = { id }
            )
            .slice(question.id, event.name.alias("event_name"), program.name.alias("program_name"), tq[todo_question.display_name], tq[todo_question.comment], question.created, question.updated)
            .selectAll()
            .prepareSQL(QueryBuilder(false))
        val total = "SELECT COUNT(*) AS count FROM ($done_aggr_sql UNION ALL $todo_aggr_sql) AS T"
            .execAndMap { it.getInt("count") }.firstOrNull()?: 0
        val offset = (page - 1) * per
        Pair("$done_aggr_sql UNION ALL $todo_aggr_sql ORDER BY created DESC LIMIT $per OFFSET $offset "
            .execAndMap { rs ->
                QuestionAggr(rs.getBytes("id"),
                    rs.getString("event_name"),
                    rs.getString("program_name"),
                    rs.getString("display_name"),
                    rs.getString("comment"),
                    rs.getString("created"),
                    rs.getString("updated")
                )
            }, total)
    }
    override fun findById(id: UUID): QuestionAggr? {
        TODO()
    }
    override fun insert(comment: String): Unit = transaction {
        val now = getNowDateTimeJst()
        val nowProgram = program.select{
            (program.start_at lessEq now) and (program.end_at greaterEq now)
        }.firstOrNull()
        val question = NewQuestion.new {
            created = now
            updated = now
        }
        NewTodoQuestion(
            question_id = question.id,
            program_id = nowProgram?.toProgram()?.id ?: unknownProgram.id,
            display_name = "", // TODO
            comment = comment
        )
    }
}