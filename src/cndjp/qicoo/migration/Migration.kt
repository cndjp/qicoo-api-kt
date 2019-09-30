package cndjp.qicoo.migration

import cndjp.qicoo.infrastructure.rdb.model.question.question
import cndjp.qicoo.rdb.context.initMysqlClient
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.transaction

suspend fun main(args: Array<String>) {
    initMysqlClient()
    transaction {
        SchemaUtils.create(question)
    }
}