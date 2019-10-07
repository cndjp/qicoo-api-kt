package cndjp.qicoo.api.controller.question

import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetOrderParameter
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetParameter
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetSortParameter
import cndjp.qicoo.api.http_resource.request.question.QuestionRequest
import cndjp.qicoo.api.http_resource.response.question.QuestionListResponse
import cndjp.qicoo.api.http_resource.response.question.QuestionResponse
import cndjp.qicoo.api.service.question.QuestionService
import cndjp.qicoo.api.service.question.QuestionServiceImpl
import cndjp.qicoo.domain.repository.like_count.LikeCountRepository
import cndjp.qicoo.domain.repository.like_count.LikeCountRepositoryImpl
import cndjp.qicoo.domain.repository.question_aggr.QuestionAggrRepository
import cndjp.qicoo.domain.repository.question_aggr.QuestionAggrRepositoryImpl
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
import cndjp.qicoo.utils.EntityResult

fun Route.questionController() = questionController(Kodein {
    val kodein = Kodein {
        bind<QuestionAggrRepository>() with singleton { QuestionAggrRepositoryImpl() }
        bind<LikeCountRepository>() with singleton { LikeCountRepositoryImpl() }
    }
    bind<QuestionService>() with singleton { QuestionServiceImpl(kodein) }
})

fun Route.questionController(kodein: Kodein) {
    val questionService by kodein.instance<QuestionService>()

    route("/questions") {
        get {
            val param = QuestionGetParameter(
                per = call.parameters["per"]?.toInt() ?: 10,
                page = call.parameters["page"]?.toInt() ?: 1,
                sort = when (call.parameters["sort"]) {
                    "created" -> QuestionGetSortParameter.created
                    "like" -> QuestionGetSortParameter.like
                    else -> QuestionGetSortParameter.created
                },
                order = when (call.parameters["order"]) {
                    "asc" -> QuestionGetOrderParameter.asc
                    "desc" -> QuestionGetOrderParameter.desc
                    else -> QuestionGetOrderParameter.desc
                }
            )
            call.respond(
                HttpStatusCode.OK,
                questionService.getAll(param)
                    .let { result ->
                        QuestionListResponse(
                            result.list.map { dto ->
                                QuestionResponse(dto)
                            },
                            result.count
                        )
                    }
            )
        }
        post {
            val validRequest = runCatching {
                call.receive<QuestionRequest>()
            }
            validRequest
                .onSuccess { validatedRequest ->
                    when (questionService.createQuestion(validatedRequest.comment)) {
                        EntityResult.Success -> call.respond(HttpStatusCode.OK)
                        EntityResult.NotFoundEntityFailure -> call.respond(HttpStatusCode.BadRequest, EntityResult.NotFoundEntityFailure.returnReason())
                    }
                }
                .onFailure { exception ->
                    call.respond(HttpStatusCode.BadRequest, "invalid json format: $exception")
                }
        }
        post("/answer") {
            val questionId = call.parameters["question_id"]?.toInt() ?: 0
            when (questionService.answer(questionId)) {
                EntityResult.Success -> call.respond(HttpStatusCode.OK)
                EntityResult.NotFoundEntityFailure -> call.respond(HttpStatusCode.BadRequest, EntityResult.NotFoundEntityFailure.returnReason())
            }
        }
        post("/like") {
            val questionId = call.parameters["question_id"]?.toInt() ?: 0
            when (questionService.incr(questionId)) {
                EntityResult.Success -> call.respond(HttpStatusCode.OK)
                EntityResult.NotFoundEntityFailure -> call.respond(HttpStatusCode.BadRequest, EntityResult.NotFoundEntityFailure.returnReason())
            }
        }
        get("/detail") {
            call.respond(HttpStatusCode.OK, "question detail routing ok")
        }
    }
}
