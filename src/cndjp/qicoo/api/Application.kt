package cndjp.qicoo.api

import cndjp.qicoo.api.controller.healthcheck.healthCheckController
import cndjp.qicoo.api.controller.question.questionController
import cndjp.qicoo.rdb.context.initMysqlClient
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    initMysqlClient()

    val server = embeddedServer(Netty, 8080) {
        routing {
            questionController()
            healthCheckController()
        }
    }
    server.start()
}