package domain.repository.question_aggr

import domain.dao.program.toProgram
import domain.dao.program.unknownProgram
import domain.dao.question.NewQuestion
import domain.dao.question_aggr.QuestionAggr
import domain.dao.question_aggr.QuestionAggrList
import domain.dao.question_aggr.toQuestionAggrFromDone
import domain.dao.question_aggr.toQuestionAggrFromTodo
import domain.dao.todo_question.NewTodoQuestion
import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.program.program
import domain.model.question.question
import domain.model.todo_question.todo_question
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import utils.execAndMap
import utils.getNowDateTimeJst
import utils.orWhere

class QuestionAggrRepositoryImpl : QuestionAggrRepository {
    // ExposedにUNIONとかWITHないから生SQL使う。
    // パフォーマンス悪かったらもうjooqでやるしかないけど...
    override fun findAll(per: Int, page: Int, order: String): QuestionAggrList = transaction {
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
        QuestionAggrList(
            "$done_aggr_sql UNION ALL $todo_aggr_sql ORDER BY created $order LIMIT $per OFFSET $offset "
                .execAndMap { rs ->
                    QuestionAggr(
                        rs.getInt("id"),
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
    override fun findById(id: Int): QuestionAggr? {
        TODO()
    }

    override fun findByIds(ids: List<Int>): QuestionAggrList = transaction {
        require(ids.isNotEmpty())

        val total = question.selectAll().count()
        val done_query = question
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
        .slice(question.id, event.name, program.name, done_question.display_name, done_question.comment, done_question.program_id, question.created, question.updated)
        .selectAll()
        .also { prepare -> ids.map { prepare.orWhere { done_question.question_id eq it } } }
        .map { it.toQuestionAggrFromDone() }

        val todo_sql = question
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
            .slice(question.id, event.name, program.name, todo_question.display_name, todo_question.comment, todo_question.program_id, question.created, question.updated)
            .selectAll()
            .also { prepare -> ids.map { prepare.orWhere { todo_question.question_id eq it } } }
            .map { it.toQuestionAggrFromTodo() }

        QuestionAggrList(
            listOf(done_query, todo_sql).flatten(), total
        )
    }

    override fun insert(comment: String): NewTodoQuestion? = transaction {
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
