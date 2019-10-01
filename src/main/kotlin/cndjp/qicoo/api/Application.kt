package api

import io.ktor.application.Application
import api.controller.healthcheck.healthCheckController
import api.controller.question.questionController
import infrastructure.rdb.client.initMysqlClient
import io.ktor.routing.routing

fun Application.main() {
    initMysqlClient()
    routing {
        questionController()
        healthCheckController()
    }
}