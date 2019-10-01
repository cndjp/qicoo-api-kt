package migration.clean

import domain.dao.done_question.done_question
import domain.dao.event.event
import domain.dao.linked_user.linked_user
import domain.dao.program.program
import domain.dao.question.question
import domain.dao.reply.reply
import domain.dao.todo_question.todo_question
import domain.dao.unlinked_user.unlinked_user
import domain.dao.user.user
import infrastructure.rdb.client.initMysqlClient
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.transaction

suspend fun main(args: Array<String>) {
    initMysqlClient()
    transaction {
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