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
            val per = call.parameters["per"]?.toInt() ?: 10
            val page = call.parameters["page"]?.toInt() ?: 1
            val result = questionService.getAll(per, page)
            call.respond(HttpStatusCode.OK, QuestionListResponse(result.first.map{QuestionResponse(it)}, result.second))
        }
        post {
            val validRequest = runCatching {
                call.receive<QuestionRequest>()
            }
            validRequest
                .onSuccess { request ->
                    questionService.create(request.comment)
                }
                .onFailure { exception ->
                    call.respond(HttpStatusCode.BadRequest, "invalid json format: $exception")
                }
        }
        get("/detail") {
            call.respond(HttpStatusCode.OK, "question detail routing ok")
        }
    }
}