package test.kotlin.cndjp.qicoo.domain

import main.kotlin.cndjp.qicoo.domain.dao.event.event
import main.kotlin.cndjp.qicoo.domain.dao.question.question
import main.kotlin.cndjp.qicoo.domain.entity.event.Event
import main.kotlin.cndjp.qicoo.domain.entity.event.toEntity as toEvent
import main.kotlin.cndjp.qicoo.domain.entity.question.Question
import main.kotlin.cndjp.qicoo.domain.entity.question.toEntity as toQuestion
import main.kotlin.cndjp.qicoo.infrastructure.rdb.client.initMysqlClient
import main.kotlin.cndjp.qicoo.utils.toDatetimeJst
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
            val now = DateTime.now()
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
                assertEquals(r1.created, now)
                assertEquals(r1.updated,now)

                val updatedCount = question.update({question.created eq now}) {
                    it[updated] = oneMilliSecondAgo
                }
                assertEquals(updatedCount,1)

                val r2 = question.select {question.updated eq oneMilliSecondAgo}.map{it.toQuestion()}.first()
                assertEquals(r2.created, now)
                assertEquals(r2.updated, oneMilliSecondAgo)

                val rl: MutableList<Question> =  mutableListOf()
                question.selectAll().orderBy(question.created).forEach{
                    rl.add(it.toQuestion())
                }
                assertEquals(rl[1].created, now)
                assertEquals(rl[1].updated, oneMilliSecondAgo)
                assertEquals(rl[0].created, yesterday)
                assertEquals(rl[0].updated, yesterday)

                val deleteCount1 = question.deleteWhere { question.created eq now }
                assertEquals(deleteCount1, 1)
                val deleteCount2 = question.deleteWhere { question.created eq yesterday }
                assertEquals(deleteCount2, 1)

                SchemaUtils.drop(
                    question
                )
            }
        }
        test("eventのCRUDテスト") {
            initMysqlClient()
            val now = DateTime.now()
            val oneMilliSecondAgo = now + Duration(1)
            val yesterday = now.minusDays(1)
            val event1StartAt = "2018-01-01 19:00:00".toDatetimeJst()
            val event1EndAt = "2018-01-01 19:00:00".toDatetimeJst()
            val event2StartAt = "2018-02-01 19:00:00".toDatetimeJst()
            val event2EndAt = "2018-02-01 19:00:00".toDatetimeJst()

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
                assertEquals(r1.start_at, event1StartAt)
                assertEquals(r1.end_at, event1EndAt)
                assertEquals(r1.created, now)
                assertEquals(r1.updated, now)

                val updatedCount = event.update({event.created eq now}) {
                    it[updated] = oneMilliSecondAgo
                }
                assertEquals(updatedCount, 1)

                val r2 = event.select {event.updated eq oneMilliSecondAgo}.map{it.toEvent()}.first()
                assertEquals(r2.start_at, event1StartAt)
                assertEquals(r2.end_at, event1EndAt)
                assertEquals(r2.created, now)
                assertEquals(r2.updated, oneMilliSecondAgo)

                val rl: MutableList<Event> =  mutableListOf()
                event.selectAll().orderBy(event.created).forEach{
                    rl.add(it.toEvent())
                }
                assertEquals(rl[1].start_at, event1StartAt)
                assertEquals(rl[1].end_at, event1EndAt)
                assertEquals(rl[1].created, now)
                assertEquals(rl[1].updated, oneMilliSecondAgo)
                assertEquals(rl[0].start_at, event2StartAt)
                assertEquals(rl[0].end_at, event2EndAt)
                assertEquals(rl[0].created, yesterday)
                assertEquals(rl[0].updated, yesterday)

                val deleteCount1 = event.deleteWhere { event.created eq now }
                assertEquals(deleteCount1, 1)
                val deleteCount2 = event.deleteWhere { event.created eq yesterday }
                assertEquals(deleteCount2, 1)

                SchemaUtils.drop(
                    event
                )
            }
        }
    }
})