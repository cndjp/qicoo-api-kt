package test.kotlin.cndjp.qicoo.domain

import main.kotlin.cndjp.qicoo.domain.dao.done_question.done_question
import main.kotlin.cndjp.qicoo.domain.dao.event.event
import main.kotlin.cndjp.qicoo.domain.dao.linked_user.linked_user
import main.kotlin.cndjp.qicoo.domain.dao.program.program
import main.kotlin.cndjp.qicoo.domain.dao.question.question
import main.kotlin.cndjp.qicoo.domain.dao.reply.reply
import main.kotlin.cndjp.qicoo.domain.dao.todo_question.todo_question
import main.kotlin.cndjp.qicoo.domain.dao.unlinked_user.unlinked_user
import main.kotlin.cndjp.qicoo.domain.dao.user.user
import main.kotlin.cndjp.qicoo.domain.entity.question.Question
import main.kotlin.cndjp.qicoo.domain.entity.question.toQuestion
import main.kotlin.cndjp.qicoo.infrastructure.rdb.client.initMysqlClient
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.spekframework.spek2.Spek


object DomainSpec: Spek({

    group("mysqlのテスト") {
        test("Questionのtest") {

            initMysqlClient()
            val now = DateTime.now()

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

                question.insert {
                    it[created] = now
                    it[updated] = now
                }

                val q = question.select {question.created eq now}.map{it.toQuestion()}.first()
                assert(q.created == now)

                SchemaUtils.drop(
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
            }
        }
    }
})