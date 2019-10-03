package api.controller.question

import api.request.question.QuestionRequest
import api.response.quesion.QuestionListResponse
import api.response.quesion.QuestionResponse
import api.service.question.QuestionService
import api.service.question.QuestionServiceImpl
import domain.repository.like_count.LikeCountRepository
import domain.repository.like_count.LikeCountRepositoryImpl
import domain.repository.question_aggr.QuestionAggrRepository
import domain.repository.question_aggr.QuestionAggrRepositoryImpl
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import utils.RetResult

fun Route.questionController() = questionController(Kodein {
    val kodein = Kodein {
        bind<QuestionAggrRepository>() with singleton { QuestionAggrRepositoryImpl() }
        bind<LikeCountRepository>() with singleton { LikeCountRepositoryImpl() }
    }
    bind<QuestionService>() with singleton { QuestionServiceImpl(kodein) }
})

fun Route.questionController(kodein: Kodein) {
    val questionService by kodein.instance<QuestionService>()

    route("/question") {
        get {
            call.respond(HttpStatusCode.OK, QuestionListResponse(questionService.getAll().map{QuestionResponse(it)}))
        }
        post {
            val request = call.receive<QuestionRequest>()
            println(request)
            when (questionService.create(request.comment)) {
                RetResult.Success -> call.respond(HttpStatusCode.OK)
                RetResult.NotFoundEntityFailure -> call.respond(HttpStatusCode.InternalServerError)
                else -> call.respond(HttpStatusCode.InternalServerError)
            }
        }
        get("/detail") {
            call.respond(HttpStatusCode.OK, "question detail routing ok")
        }
    }
}