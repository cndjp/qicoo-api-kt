package api

import io.ktor.application.Application
import api.controller.healthcheck.healthCheckController
import api.controller.question.questionController
import main.kotlin.cndjp.qicoo.infrastructure.rdb.client.initMysqlClient
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.main() {
    initMysqlClient()
    routing {
        questionController()
        healthCheckController()
    }
}