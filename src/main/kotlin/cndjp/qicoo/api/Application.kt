package cndjp.qicoo.api

import cndjp.qicoo.api.controller.healthcheck.healthCheckController
import cndjp.qicoo.api.controller.question.questionController
import cndjp.qicoo.api.service.question.QuestionService
import cndjp.qicoo.api.service.question.QuestionServiceImpl
import cndjp.qicoo.domain.model.event.event
import cndjp.qicoo.domain.model.program.program
import cndjp.qicoo.domain.model.question.question
import cndjp.qicoo.domain.repository.like_count.LikeCountRepository
import cndjp.qicoo.domain.repository.question_aggr.QuestionAggrRepository
import cndjp.qicoo.domain.repository.reply.ReplyRepository
import cndjp.qicoo.infrastructure.rdb.client.initMysqlClient
import cndjp.qicoo.infrastructure.repository.LikeCountRepositoryImpl
import cndjp.qicoo.infrastructure.repository.QuestionAggrRepositoryImpl
import cndjp.qicoo.infrastructure.repository.ReplyRepositoryImpl
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Duration
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.main() {
    initMysqlClient()
    transaction {
        SchemaUtils.create(
            question,
            event,
            program
        )
    }
    install(CallLogging)
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }
    install(CORS) {
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Options)
        header(HttpHeaders.ContentType)
        header(HttpHeaders.ContentLength)
        header(HttpHeaders.Authorization)
        anyHost() // そのうち絞る
        allowCredentials = true
        allowNonSimpleContentTypes = true
        maxAge = Duration.ofDays(1)
    }
    val serviceKodein = Kodein {
        val repoKodein = Kodein {
            bind<QuestionAggrRepository>() with singleton { QuestionAggrRepositoryImpl() }
            bind<LikeCountRepository>() with singleton { LikeCountRepositoryImpl() }
            bind<ReplyRepository>() with singleton { ReplyRepositoryImpl() }
        }
        bind<QuestionService>() with singleton { QuestionServiceImpl(repoKodein) }
    }
    routing {
        questionController(serviceKodein)
        healthCheckController()
    }
}
