package api.controller.question

import api.response.quesion.factory
import api.service.question.QuestionService
import api.service.question.QuestionServiceImpl
import domain.repository.done_question.DoneQuestionRepository
import domain.repository.done_question.DoneQuestionRepositoryImpl
import domain.repository.event.EventRepository
import domain.repository.event.EventRepositoryImpl
import domain.repository.event.ProgramRepository
import domain.repository.event.ProgramRepositoryImpl
import domain.repository.question.QuestionRepository
import domain.repository.question.QuestionRepositoryImpl
import domain.repository.todo_question.TodoQuestionRepository
import domain.repository.todo_question.TodoQuestionRepositoryImpl
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
        bind<QuestionRepository>() with singleton { QuestionRepositoryImpl() }
        bind<EventRepository>() with singleton { EventRepositoryImpl() }
        bind<ProgramRepository>() with singleton { ProgramRepositoryImpl() }
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