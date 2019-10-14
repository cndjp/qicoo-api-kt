package cndjp.qicoo.domain.repository.question_aggr

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.api.withLog
import cndjp.qicoo.domain.dao.program.toProgram
import cndjp.qicoo.domain.dao.program.unknownProgram
import cndjp.qicoo.domain.dao.question.NewQuestionId
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
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.insertIgnoreAndGetId
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
                otherColumn = { question.program_id },
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

    override fun checkExistById(id: Int): Result<Unit, QicooError> = transaction {
        question
            .select { question.id eq id }
            .count()
            .let {
                when (it) {
                    0 -> Err(QicooError.CouldNotCreateEntityFailure.withLog())
                    else -> Ok(Unit)
                }
            }
    }

    override fun findById(id: Int): Result<QuestionAggr, QicooError> = transaction {
        question
            .innerJoin(
                otherTable = program.innerJoin(
                    otherTable = event,
                    otherColumn = { program.event_id },
                    onColumn = { event.id }
                ),
                otherColumn = { question.program_id },
                onColumn = { program.id }
            )
            .slice(question.id, event.name, program.name, question.done_flag, question.display_name, question.comment, question.program_id, question.created, question.updated)
            .select { question.id eq id }
            .firstOrNull()
            .toResultOr {
                QicooError.NotFoundEntityFailure.withLog()
            }
            .flatMap { Ok(it.toQuestionAggr()) }
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
                    otherColumn = { question.program_id },
                    onColumn = { program.id }
                )
                .slice(question.id, event.name, program.name, question.done_flag, question.display_name, question.comment, question.program_id, question.created, question.updated)
                .selectAll()
            .also { prepare -> ids.map { prepare.orWhere { question.id eq it } } }
            .map { it.toQuestionAggr() }, total))
    }

    override fun insert(comment: String): Result<NewQuestionId, QicooError> = transaction {
        val now = getNowDateTimeJst()
        program.select {
            (program.start_at lessEq now) and (program.end_at greaterEq now)
        }
            .firstOrNull()
            .let { programRow ->
                val new_prgram_id = programRow?.toProgram()?.id ?: unknownProgram.id
                val new_done_flag = false
                val new_display_name = "anonymous" // TODO
                question.insertIgnoreAndGetId {
                    it[program_id] = new_prgram_id
                    it[done_flag] = new_done_flag
                    it[display_name] = new_display_name
                    it[this.comment] = comment
                    it[created] = now
                    it[updated] = now
                }
                    .toResultOr { QicooError.CouldNotCreateEntityFailure.withLog() }
                    .flatMap { Ok(it.value) }
            }
    }

    override fun todo2done(id: Int): Result<Unit, QicooError> = transaction {
        when (question.update({ (question.id eq id) and (question.done_flag eq false) }) { it[question.done_flag] = true }) {
            0 -> Err(QicooError.CouldNotCreateEntityFailure.withLog())
            else -> Ok(Unit)
        }
    }
}
