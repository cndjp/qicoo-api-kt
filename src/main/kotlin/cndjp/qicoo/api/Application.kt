package main.kotlin.cndjp.qicoo.api

import main.kotlin.cndjp.qicoo.api.controller.healthcheck.healthCheckController
import main.kotlin.cndjp.qicoo.api.controller.question.questionController
import main.kotlin.cndjp.qicoo.infrastructure.rdb.client.initMysqlClient
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