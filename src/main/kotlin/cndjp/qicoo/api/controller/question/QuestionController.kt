package api.controller.question

import api.response.quesion.factory
import api.service.question.QuestionService
import api.service.question.QuestionServiceImpl
import domain.repository.question_aggr.QuestionAggrRepository
import domain.repository.question_aggr.QuestionAggrRepositoryImpl
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun Route.questionController() = questionController(Kodein {
    val kodein = Kodein {
        bind<QuestionAggrRepository>() with singleton { QuestionAggrRepositoryImpl() }
    }
    bind<QuestionService>() with singleton { QuestionServiceImpl(kodein) }
})

fun Route.questionController(kodein: Kodein) {
    val service by kodein.instance<QuestionService>()

    route("/question") {
        get {
            call.respond(HttpStatusCode.OK, service.getAll().factory())
        }
        get("/detail") {
            call.respond(HttpStatusCode.OK, "question detail routing ok")
        }
    }
}