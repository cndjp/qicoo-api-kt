package test.cndjp.qicoo.api

import cndjp.qicoo.api.controller.question.questionController
import cndjp.qicoo.api.service.question.QuestionService
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import test.cndjp.qicoo.api.service.QuestionServiceMock

fun Application.mock() {
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }
    val mockServiceKodein = Kodein {
        bind<QuestionService>() with singleton { QuestionServiceMock() }
    }
    routing {
        questionController(mockServiceKodein)
    }
}