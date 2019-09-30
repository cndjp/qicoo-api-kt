package cndjp.qicoo.migration.clean

import cndjp.qicoo.domain.dao.done_question.done_question
import cndjp.qicoo.domain.dao.event.event
import cndjp.qicoo.domain.dao.linked_user.linked_user
import cndjp.qicoo.domain.dao.program.program
import cndjp.qicoo.domain.dao.question.question
import cndjp.qicoo.domain.dao.reply.reply
import cndjp.qicoo.domain.dao.todo_question.todo_question
import cndjp.qicoo.domain.dao.unlinked_user.unlinked_user
import cndjp.qicoo.domain.dao.user.user
import cndjp.qicoo.infrastructure.rdb.client.initMysqlClient
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.transaction

suspend fun main(args: Array<String>) {
    initMysqlClient()
    transaction {
        SchemaUtils.drop(question, reply, done_question, todo_question, event, program, user, linked_user, unlinked_user)
    }
}