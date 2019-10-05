package domain.repository.question_aggr

import domain.dao.program.toProgram
import domain.dao.program.unknownProgram
import domain.dao.question.NewQuestion
import domain.dao.question_aggr.QuestionAggr
import domain.dao.question_aggr.QuestionAggrList
import domain.dao.todo_question.NewTodoQuestion
import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.program.program
import domain.model.question.question
import domain.model.todo_question.todo_question
import java.util.UUID
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import utils.execAndMap
import utils.getNowDateTimeJst

class QuestionAggrRepositoryImpl : QuestionAggrRepository {
    // ExposedにUNIONとかWITHないから生SQL使う。
    // パフォーマンス悪かったらもうjooqでやるしかないけど...
    override fun findAll(per: Int, page: Int, isSort: Boolean, order: String): QuestionAggrList = transaction {
        val done_aggr_sql = question
            .innerJoin(
                otherTable = done_question.innerJoin(
                    otherTable = program.innerJoin(
                        otherTable = event,
                        otherColumn = { program.event_id },
                        onColumn = { event.id }
                    ),
                    otherColumn = { done_question.program_id },
                    onColumn = { program.id }
                ),
                otherColumn = { done_question.question_id },
                onColumn = { id }
            )
            .slice(question.id, event.name.alias("event_name"), program.name.alias("program_name"), done_question.display_name, done_question.comment, done_question.program_id, question.created, question.updated)
            .selectAll()
            .prepareSQL(QueryBuilder(false))
        val todo_aggr_sql = question
            .innerJoin(
                otherTable = todo_question.innerJoin(
                    otherTable = program.innerJoin(
                        otherTable = event,
                        otherColumn = { program.event_id },
                        onColumn = { event.id }
                    ),
                    otherColumn = { todo_question.program_id },
                    onColumn = { program.id }
                ),
                otherColumn = { todo_question.question_id },
                onColumn = { id }
            )
            .slice(question.id, event.name.alias("event_name"), program.name.alias("program_name"), todo_question.display_name, todo_question.comment, todo_question.program_id, question.created, question.updated)
            .selectAll()
            .prepareSQL(QueryBuilder(false))
        val total = question.selectAll().count()
        val offset = (page - 1) * per
        val sortStr = when (isSort) {
            true -> "ORDER BY created $order"
            false -> ""
        }
        QuestionAggrList(
            "$done_aggr_sql UNION ALL $todo_aggr_sql $sortStr LIMIT $per OFFSET $offset "
                .execAndMap { rs ->
                    QuestionAggr(
                        rs.getBytes("id"),
                        rs.getString("event_name"),
                        rs.getString("program_name"),
                        rs.getString("display_name"),
                        rs.getString("comment"),
                        rs.getString("created"),
                        rs.getString("updated")
                    )
                }, total
        )
    }
    override fun findById(id: UUID): QuestionAggr? {
        TODO()
    }
    override fun insert(comment: String): Unit = transaction {
        val now = getNowDateTimeJst()
        val nowProgram = program.select {
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
