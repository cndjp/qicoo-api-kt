package cndjp.qicoo.api

import cndjp.qicoo.api.controller.healthcheck.healthCheckController
import cndjp.qicoo.api.controller.question.questionController
import cndjp.qicoo.api.service.question.QuestionService
import cndjp.qicoo.api.service.question.QuestionServiceImpl
import cndjp.qicoo.domain.repository.like_count.LikeCountRepository
import cndjp.qicoo.domain.repository.like_count.LikeCountRepositoryImpl
import cndjp.qicoo.domain.repository.question_aggr.QuestionAggrRepository
import cndjp.qicoo.domain.repository.question_aggr.QuestionAggrRepositoryImpl
import com.fasterxml.jackson.databind.SerializationFeature
import cndjp.qicoo.infrastructure.rdb.client.initMysqlClient
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.main() {
    val _client = initMysqlClient()
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }
    val serviceKodein = Kodein {
        val repoKodein = Kodein {
            bind<QuestionAggrRepository>() with singleton { QuestionAggrRepositoryImpl() }
            bind<LikeCountRepository>() with singleton { LikeCountRepositoryImpl() }
        }
        bind<QuestionService>() with singleton { QuestionServiceImpl(repoKodein) }
    }
    routing {
        questionController(serviceKodein)
        healthCheckController()
    }
}
