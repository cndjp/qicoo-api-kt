package domain

import domain.dao.event.event
import domain.dao.program.program
import domain.entity.program.Program
import domain.entity.program.toEntity as toProgram
import domain.dao.question.question
import domain.entity.event.Event
import domain.entity.event.toEntity as toEvent
import domain.entity.question.Question
import domain.entity.question.toEntity as toQuestion
import infrastructure.rdb.client.initMysqlClient
import domain.dao.user.user
import domain.entity.done_question.DoneQuestion
import domain.entity.user.User
import domain.entity.user.toEntity as toUser
import utils.getNowDateTimeJst
import utils.toDateTimeJst
import utils.toJST
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.Duration
import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek

object DomainSpec: Spek({
    group("mysqlのテスト") {
        test("questionのCRUDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val oneMilliSecondAgo = now + Duration(1)
            val yesterday = now.minusDays(1)

            transaction {
                SchemaUtils.create(
                    question
                )

                question.insert {
                    it[created] = now
                    it[updated] = now
                }

                question.insert {
                    it[created] = yesterday
                    it[updated] = yesterday
                }

                val r1 = question.select { question.created eq now}.map{it.toQuestion()}.first()
                assertEquals(now, r1.created.toJST())
                assertEquals(now, r1.updated.toJST())

                val updatedCount = question.update({ question.created eq now}) {
                    it[updated] = oneMilliSecondAgo
                }
                assertEquals(updatedCount,1)

                val rl: MutableList<Question> =  mutableListOf()
                question.selectAll().orderBy(question.created to SortOrder.DESC).forEach{
                    rl.add(it.toQuestion())
                }
                assertEquals(now, rl[0].created.toJST())
                assertEquals(oneMilliSecondAgo, rl[0].updated.toJST())
                assertEquals(yesterday, rl[1].created.toJST())
                assertEquals(yesterday, rl[1].updated.toJST())

                val deleteCount1 = question.deleteWhere { question.created eq now }
                assertEquals(1, deleteCount1)
                val deleteCount2 = question.deleteWhere { question.created eq yesterday }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    question
                )
            }
        }
        test("eventのCRUDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val oneMilliSecondAgo = now + Duration(1)
            val yesterday = now.minusDays(1)
            val event1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
            val event1EndAt = "2018-01-01 19:00:00".toDateTimeJst()
            val event2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
            val event2EndAt = "2018-02-01 19:00:00".toDateTimeJst()

            transaction {
                SchemaUtils.create(
                    event
                )

                event.insert {
                    it[start_at] = event1StartAt
                    it[end_at]  = event1EndAt
                    it[created] = now
                    it[updated] = now
                }

                event.insert {
                    it[start_at] = event2StartAt
                    it[end_at]  = event2EndAt
                    it[created] = yesterday
                    it[updated] = yesterday
                }

                val r1 = event.select { event.created eq now}.map{it.toEvent()}.first()
                assertEquals(event1StartAt, r1.start_at.toJST())
                assertEquals(event1EndAt, r1.end_at.toJST())
                assertEquals(now, r1.created.toJST())
                assertEquals(now, r1.updated.toJST())

                val updatedCount = event.update({ event.created eq now}) {
                    it[updated] = oneMilliSecondAgo
                }
                assertEquals(1, updatedCount)

                val rl: MutableList<Event> =  mutableListOf()
                event.selectAll().orderBy(event.created to SortOrder.DESC).forEach{
                    rl.add(it.toEvent())
                }
                assertEquals(event1StartAt, rl[0].start_at.toJST())
                assertEquals(event1EndAt, rl[0].end_at.toJST())
                assertEquals(now, rl[0].created.toJST())
                assertEquals(oneMilliSecondAgo, rl[0].updated.toJST())
                assertEquals(event2StartAt, rl[1].start_at.toJST())
                assertEquals(event2EndAt, rl[1].end_at.toJST())
                assertEquals(yesterday, rl[1].created.toJST())
                assertEquals(yesterday, rl[1].updated.toJST())

                val deleteCount1 = event.deleteWhere { event.created eq now }
                assertEquals(1, deleteCount1)
                val deleteCount2 = event.deleteWhere { event.created eq yesterday }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    event
                )
            }
        }
        test("programのCRUDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val oneMilliSecondAgo = now + Duration(1)
            val yesterday = now.minusDays(1)
            val event1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
            val event1EndAt = "2018-01-02 19:00:00".toDateTimeJst()
            val event2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
            val event2EndAt = "2018-02-02 19:00:00".toDateTimeJst()
            val program1StartAt = "2018-01-01 19:00:00".toDateTimeJst()
            val program1EndAt = "2018-01-01 19:30:00".toDateTimeJst()
            val program2StartAt = "2018-02-01 19:00:00".toDateTimeJst()
            val program2EndAt = "2018-02-01 19:30:00".toDateTimeJst()

            transaction {
                SchemaUtils.create(
                    event,
                    program
                )

                event.insert {
                    it[start_at] = event1StartAt
                    it[end_at]  = event1EndAt
                    it[created] = now
                    it[updated] = now
                }

                event.insert {
                    it[start_at] = event2StartAt
                    it[end_at]  = event2EndAt
                    it[created] = yesterday
                    it[updated] = yesterday
                }

                val rl: MutableList<Event> =  mutableListOf()
                event.selectAll().orderBy(event.created to SortOrder.DESC).forEach{
                    rl.add(it.toEvent())
                }
                assertEquals(event1StartAt, rl[0].start_at.toJST())
                assertEquals(event1EndAt, rl[0].end_at.toJST())
                assertEquals(now, rl[0].created.toJST())
                assertEquals(now, rl[0].updated.toJST())
                assertEquals(event2StartAt, rl[1].start_at.toJST())
                assertEquals(event2EndAt, rl[1].end_at.toJST())
                assertEquals(yesterday, rl[1].created.toJST())
                assertEquals(yesterday, rl[1].updated.toJST())

                program.insert {
                    it[event_id] = rl[0].id
                    it[start_at] = program1StartAt
                    it[end_at]  = program1EndAt
                    it[created] = now
                    it[updated] = now
                }

                program.insert {
                    it[event_id] = rl[1].id
                    it[start_at] = program2StartAt
                    it[end_at]  = program2EndAt
                    it[created] = yesterday
                    it[updated] = yesterday
                }

                val r1 = program.select { program.created eq now}.map{it.toProgram()}.first()
                assertEquals(rl[0].id, r1.event_id)
                assertEquals(program1StartAt, r1.start_at.toJST())
                assertEquals(program1EndAt, r1.end_at.toJST())
                assertEquals(now, r1.created.toJST())
                assertEquals(now, r1.updated.toJST())

                val updatedCount = program.update({ program.created eq now}) {
                    it[updated] = oneMilliSecondAgo
                }
                assertEquals(1, updatedCount)

                val srl: MutableList<Program> =  mutableListOf()
                program.selectAll().orderBy(program.created to SortOrder.DESC).forEach{
                    srl.add(it.toProgram())
                }
                assertEquals(rl[0].id, srl[0].event_id)
                assertEquals(program1StartAt, srl[0].start_at.toJST())
                assertEquals(program1EndAt, srl[0].end_at.toJST())
                assertEquals(now, srl[0].created.toJST())
                assertEquals(oneMilliSecondAgo, srl[0].updated.toJST())
                assertEquals(rl[1].id, srl[1].event_id)
                assertEquals(program2StartAt, srl[1].start_at.toJST())
                assertEquals(program2EndAt, srl[1].end_at.toJST())
                assertEquals(yesterday, srl[1].created.toJST())
                assertEquals(yesterday, srl[1].updated.toJST())


                val deleteCount1 = program.deleteWhere { program.created eq now }
                assertEquals(1, deleteCount1)
                val deleteCount2 = program.deleteWhere { program.created eq yesterday }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    event,
                    program
                )
            }
        }
        test("userのCRDテスト") {
            initMysqlClient()
            val now = getNowDateTimeJst()
            val oneMilliSecondAgo = now + Duration(1)
            val yesterday = now.minusDays(1)

            transaction {
                SchemaUtils.create(
                    user
                )

                user.insert {
                    it[created] = now
                }

                user.insert {
                    it[created] = yesterday
                }

                val r1 = user.select { user.created eq now}.map{it.toUser()}.first()
                assertEquals(now, r1.created.toJST())

                val rl: MutableList<User> =  mutableListOf()
                user.selectAll().orderBy(user.created to SortOrder.DESC).forEach{
                    rl.add(it.toUser())
                }
                assertEquals(now, rl[0].created.toJST())
                assertEquals(yesterday, rl[1].created.toJST())

                val deleteCount1 = user.deleteWhere { user.created eq now }
                assertEquals(1, deleteCount1)
                val deleteCount2 = user.deleteWhere { user.created eq yesterday }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    user
                )
            }
        }
    }
})