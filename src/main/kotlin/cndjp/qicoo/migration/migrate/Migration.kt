package migration.migrate

import domain.dao.done_question.DoneQuestion
import domain.dao.done_question.NewDoneQuestion
import domain.dao.done_question.toDoneQuestion
import domain.dao.event.NewEvent
import domain.dao.program.NewProgram
import domain.dao.question.NewQuestion
import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.linked_user.linked_user
import domain.model.program.program
import domain.model.question.question
import domain.model.reply.reply
import domain.model.todo_question.todo_question
import domain.model.unlinked_user.unlinked_user
import domain.model.user.user
import infrastructure.rdb.client.initMysqlClient
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.transaction
import utils.getNowDateTimeJst
import utils.toDateTimeJst

suspend fun main(args: Array<String>) {
    initMysqlClient()
    transaction {
        SchemaUtils.create(
            question,
            reply,
            done_question,
            todo_question,
            event,
            program,
            user,
            linked_user,
            unlinked_user
        )
        val now = getNowDateTimeJst()
        val yesterday = now.minusDays(1)
        val event1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
        val event1EndAt = "2018-01-02 19:00:00".toDateTimeJst()
        val event2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
        val event2EndAt = "2018-02-02 19:00:00".toDateTimeJst()
        val program1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
        val program1EndAt = "2019-01-01 19:30:00".toDateTimeJst()
        val program2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
        val program2EndAt = "2018-02-01 19:30:00".toDateTimeJst()
        val e1name = "poe"
        val e2name = "fae"
        val p1name = "poke"
        val p2name = "moka"
        val d1name = "nyan"
        val d1likes = 10
        val d1likesUpdated = 111
        val d1comment = "what is qicoo"
        val d2name = "hyon"
        val d2likes = 200
        val d2comment = "what is mayo"


        org.jetbrains.exposed.sql.transactions.transaction {
            SchemaUtils.create(
                event,
                question,
                done_question,
                program
            )

            val e1 = NewEvent.new {
                name = e1name
                start_at = event1StartAt
                end_at = event1EndAt
                created = now
                updated = now
            }

            val e2 = NewEvent.new {
                name = e2name
                start_at = event2StartAt
                end_at = event2EndAt
                created = yesterday
                updated = yesterday
            }


            val p1 = NewProgram.new {
                name = p1name
                event_id = e1.id
                start_at = program1StartAt
                end_at = program1EndAt
                created = now
                updated = now
            }

            val p2 = NewProgram.new {
                name = p2name
                event_id = e2.id
                start_at = program2StartAt
                end_at = program2EndAt
                created = yesterday
                updated = yesterday
            }
            val q1 = NewQuestion.new {
                created = now
                updated = now
            }

            val q2 = NewQuestion.new {
                created = yesterday
                updated = yesterday
            }

            NewDoneQuestion(
                question_id = q1.id,
                program_id = p1.id,
                display_name = d1name,
                comment = d1comment
            )
            NewDoneQuestion(
                question_id = q2.id,
                program_id = p2.id,
                display_name = d2name,
                comment = d2comment
            )
        }
    }
}