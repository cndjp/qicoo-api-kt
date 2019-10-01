package test.kotlin.cndjp.qicoo.domain

import main.kotlin.cndjp.qicoo.domain.dao.event.event
import main.kotlin.cndjp.qicoo.domain.dao.question.question
import main.kotlin.cndjp.qicoo.domain.entity.event.Event
import main.kotlin.cndjp.qicoo.domain.entity.event.toEntity as toEvent
import main.kotlin.cndjp.qicoo.domain.entity.question.Question
import main.kotlin.cndjp.qicoo.domain.entity.question.toEntity as toQuestion
import main.kotlin.cndjp.qicoo.infrastructure.rdb.client.initMysqlClient
import main.kotlin.cndjp.qicoo.utils.getNowDateTimeJst
import main.kotlin.cndjp.qicoo.utils.toDateTimeJst
import main.kotlin.cndjp.qicoo.utils.toJST
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.Duration
import org.junit.jupiter.api.Assertions.assertEquals
import org.spekframework.spek2.Spek
import java.util.*

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

                val r1 = question.select {question.created eq now}.map{it.toQuestion()}.first()
                assertEquals(now, r1.created.toJST())
                assertEquals(now, r1.updated.toJST())

                val updatedCount = question.update({question.created eq now}) {
                    it[updated] = oneMilliSecondAgo
                }
                assertEquals(updatedCount,1)

                val r2 = question.select {question.updated eq oneMilliSecondAgo}.map{it.toQuestion()}.first()
                assertEquals(now, r2.created.toJST())
                assertEquals(oneMilliSecondAgo, r2.updated.toJST())

                val rl: MutableList<Question> =  mutableListOf()
                question.selectAll().orderBy(question.created).forEach{
                    rl.add(it.toQuestion())
                }
                assertEquals(now, rl[1].created.toJST())
                assertEquals(oneMilliSecondAgo, rl[1].updated.toJST())
                assertEquals(yesterday, rl[0].created.toJST())
                assertEquals(yesterday, rl[0].updated.toJST())

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

                val r1 = event.select {event.created eq now}.map{it.toEvent()}.first()
                assertEquals(event1StartAt, r1.start_at.toJST())
                assertEquals(event1EndAt, r1.end_at.toJST())
                assertEquals(now, r1.created.toJST())
                assertEquals(now, r1.updated.toJST())

                val updatedCount = event.update({event.created eq now}) {
                    it[updated] = oneMilliSecondAgo
                }
                assertEquals(1, updatedCount)

                val r2 = event.select {event.updated eq oneMilliSecondAgo}.map{it.toEvent()}.first()
                assertEquals(event1StartAt, r2.start_at.toJST())
                assertEquals(event1EndAt, r2.end_at.toJST())
                assertEquals(now, r2.created.toJST())
                assertEquals(oneMilliSecondAgo, r2.updated.toJST())

                val rl: MutableList<Event> =  mutableListOf()
                event.selectAll().orderBy(event.created).forEach{
                    rl.add(it.toEvent())
                }
                assertEquals(event1StartAt, rl[1].start_at.toJST())
                assertEquals(event1EndAt, rl[1].end_at.toJST())
                assertEquals(now, rl[1].created.toJST())
                assertEquals(oneMilliSecondAgo, rl[1].updated.toJST())
                assertEquals(event2StartAt, rl[0].start_at.toJST())
                assertEquals(event2EndAt, rl[0].end_at.toJST())
                assertEquals(yesterday, rl[0].created.toJST())
                assertEquals(yesterday, rl[0].updated.toJST())

                val deleteCount1 = event.deleteWhere { event.created eq now }
                assertEquals(1, deleteCount1)
                val deleteCount2 = event.deleteWhere { event.created eq yesterday }
                assertEquals(1, deleteCount2)

                SchemaUtils.drop(
                    event
                )
            }
        }
    }
})