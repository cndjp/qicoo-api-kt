package api

import api.controller.healthcheck.healthCheckController
import api.controller.question.questionController
import com.fasterxml.jackson.databind.SerializationFeature
import infrastructure.rdb.client.initMysqlClient
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.main() {
    val _client = initMysqlClient()
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }
    routing {
        questionController()
        healthCheckController()
    }
}
