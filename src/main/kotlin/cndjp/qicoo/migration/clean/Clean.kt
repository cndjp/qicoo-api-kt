package migration.clean

import domain.model.done_question.done_question
import domain.model.event.event
import domain.model.linked_user.linked_user
import domain.model.program.program
import domain.model.question.question
import domain.model.reply.reply
import domain.model.todo_question.todo_question
import domain.model.unlinked_user.unlinked_user
import domain.model.user.user
import infrastructure.cache.client.qicooGlobalJedisPool
import infrastructure.cache.context.RedisContext
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
    RedisContext.flushAll(qicooGlobalJedisPool.resource)
}
