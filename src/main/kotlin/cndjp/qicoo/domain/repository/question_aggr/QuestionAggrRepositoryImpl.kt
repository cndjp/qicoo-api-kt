package cndjp.qicoo.domain.repository.question_aggr

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.api.withLog
import cndjp.qicoo.domain.dao.program.toProgram
import cndjp.qicoo.domain.dao.program.unknownProgram
import cndjp.qicoo.domain.dao.question.NewQuestion
import cndjp.qicoo.domain.dao.question.NewQuestionResult
import cndjp.qicoo.domain.dao.question_aggr.QuestionAggr
import cndjp.qicoo.domain.dao.question_aggr.QuestionAggrList
import cndjp.qicoo.domain.dao.question_aggr.toQuestionAggr
import cndjp.qicoo.domain.model.event.event
import cndjp.qicoo.domain.model.program.program
import cndjp.qicoo.domain.model.question.question
import cndjp.qicoo.utils.getNowDateTimeJst
import cndjp.qicoo.utils.orWhere
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.flatMap
import com.github.michaelbull.result.toResultOr
import mu.KotlinLogging
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class QuestionAggrRepositoryImpl : QuestionAggrRepository {
    private val logger = KotlinLogging.logger {}

    override fun findAll(per: Int, page: Int, order: String): QuestionAggrList = transaction {
        val total = question.selectAll().count()
        QuestionAggrList(
        question
            .innerJoin(
                otherTable = program.innerJoin(
                    otherTable = event,
                    otherColumn = { program.event_id },
                    onColumn = { event.id }
                ),
                otherColumn = { question.id },
                onColumn = { program.id }
            )
            .slice(question.id, event.name, program.name, question.done_flag, question.display_name, question.comment, question.created, question.updated)
            .selectAll()
            .let {
                when (order) {
                    "asc" -> it.orderBy(question.created, SortOrder.ASC)
                    "desc" -> it.orderBy(question.created, SortOrder.DESC)
                    else -> it.orderBy(question.created, SortOrder.DESC)
                }
            }
            .limit(per, ((page - 1) * per))
            .map { it.toQuestionAggr() },
            total
        )
    }
    override fun findById(id: Int): QuestionAggr? {
        TODO()
    }

    override fun findByIds(ids: List<Int>): Result<QuestionAggrList, QicooError> = transaction {
        if (ids.isEmpty()) {
            return@transaction Err(QicooError.NotFoundEntityFailure.withLog())
        }

        val total = question.selectAll().count()
        Ok(QuestionAggrList(
            question
                .innerJoin(
                    otherTable = program.innerJoin(
                        otherTable = event,
                        otherColumn = { program.event_id },
                        onColumn = { event.id }
                    ),
                    otherColumn = { question.id },
                    onColumn = { program.id }
                )
                .slice(question.id, event.name, program.name, question.done_flag, question.display_name, question.comment, question.program_id, question.created, question.updated)
                .selectAll()
            .also { prepare -> ids.map { prepare.orWhere { question.id eq it } } }
            .map { it.toQuestionAggr() }, total))
    }

    override fun insert(comment: String): Result<NewQuestionResult, QicooError> = transaction {
        val now = getNowDateTimeJst()
        program.select {
            (program.start_at lessEq now) and (program.end_at greaterEq now)
        }
            .firstOrNull()
            .let { programRow ->
                Ok(NewQuestion.new {
                    program_id = programRow?.toProgram()?.id ?: unknownProgram.id
                    done_flg = false
                    display_name = "" // TODO
                    this.comment = comment
                    created = now
                    updated = now
                }) }
            .flatMap { Ok(NewQuestionResult(it.id.value)) }
    }

    override fun todo2done(id: Int): Result<Unit, QicooError> = transaction {
        when (question.update({ (question.id eq id) and (question.done_flag eq false) }) { it[question.done_flag] = true }) {
            0 -> Err(QicooError.CouldNotCreateEntityFailure.withLog())
            else -> Ok(Unit)
        }
    }
}
