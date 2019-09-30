package main.kotlin.cndjp.qicoo.migration.migrate

import main.kotlin.cndjp.qicoo.domain.dao.done_question.done_question
import main.kotlin.cndjp.qicoo.domain.dao.event.event
import main.kotlin.cndjp.qicoo.domain.dao.linked_user.linked_user
import main.kotlin.cndjp.qicoo.domain.dao.program.program
import main.kotlin.cndjp.qicoo.domain.dao.question.question
import main.kotlin.cndjp.qicoo.domain.dao.reply.reply
import main.kotlin.cndjp.qicoo.domain.dao.todo_question.todo_question
import main.kotlin.cndjp.qicoo.domain.dao.unlinked_user.unlinked_user
import main.kotlin.cndjp.qicoo.domain.dao.user.user
import main.kotlin.cndjp.qicoo.infrastructure.rdb.client.initMysqlClient
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.transaction

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
    }
}