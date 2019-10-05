package api.controller.question

import api.http_resource.paramater.question.QuestionGetOrderParameter
import api.http_resource.paramater.question.QuestionGetParameter
import api.http_resource.paramater.question.QuestionGetSortParameter
import api.http_resource.request.question.QuestionRequest
import api.http_resource.response.question.QuestionListResponse
import api.http_resource.response.question.QuestionResponse
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
import java.util.UUID
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

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
            //println("param: $param")
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
                    questionService.createQuestion(validatedRequest.comment)
                    call.respond(HttpStatusCode.OK)
                }
                .onFailure { exception ->
                    call.respond(HttpStatusCode.BadRequest, "invalid json format: $exception")
                }
        }
        post("/like") {
            val questionId = call.parameters["question_id"] ?: ""
            val validQuestionId = runCatching {
                UUID.fromString(questionId)
            }
            validQuestionId
                .onSuccess { validatedQuestionId ->
                    questionService.incrOrCreateLike(validatedQuestionId)
                    call.respond(HttpStatusCode.OK)
                }
                .onFailure { exception ->
                    call.respond(HttpStatusCode.BadRequest, "invalid uuid format: $exception")
                }
        }
        get("/detail") {
            call.respond(HttpStatusCode.OK, "question detail routing ok")
        }
    }
}
