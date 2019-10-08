package cndjp.qicoo.api.controller.question

import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetOrderParameter
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetParameter
import cndjp.qicoo.api.http_resource.paramater.question.QuestionGetSortParameter
import cndjp.qicoo.api.http_resource.request.question.QuestionRequest
import cndjp.qicoo.api.http_resource.response.question.QuestionListResponse
import cndjp.qicoo.api.http_resource.response.question.QuestionResponse
import cndjp.qicoo.api.service.question.QuestionService
import cndjp.qicoo.api.QicooError
import cndjp.qicoo.api.QicooErrorReason
import cndjp.qicoo.api.withLog
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.toResultOr
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

fun Route.questionController(kodein: Kodein) {
    val questionService by kodein.instance<QuestionService>()

    route("/questions") {
        get {
            val param = QuestionGetParameter(
                per = call.parameters["per"]?.toIntOrNull() ?: 10,
                page = call.parameters["page"]?.toIntOrNull() ?: 1,
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
                questionService.getAll(param)
                    .mapBoth(
                        success = { listDTO ->
                            call.respond(
                                HttpStatusCode.OK,
                                QuestionListResponse(
                                    listDTO.list.map { dto ->
                                        QuestionResponse(dto)
                                    },
                                    listDTO.count
                                )
                            )
                        },
                        failure = { call.respond(HttpStatusCode.InternalServerError) }
                    )
        }
        post {
            val validRequest = runCatching {
                call.receive<QuestionRequest>()
            }
            validRequest
                .onSuccess { validatedRequest ->
                    questionService.createQuestion(validatedRequest.comment)
                        .mapBoth(
                            success = { call.respond(HttpStatusCode.OK) },
                            failure = { call.respond(HttpStatusCode.InternalServerError) }
                        )
                }
                .onFailure { exception ->
                    call.respond(HttpStatusCode.BadRequest, QicooErrorReason.ParseRequestFailure.withLog(exception.toString()).name)
                }
        }
        post("/answer") {
            val validQuestionId = call.parameters["question_id"]?.toIntOrNull()
            validQuestionId.toResultOr {
                QicooError(cndjp.qicoo.api.QicooErrorReason.ParseRequestFailure.withLog())
            }
                .mapBoth(
                    success = { validatedQuestionId ->
                        questionService.answer(validatedQuestionId)
                            .mapBoth(
                                success = { call.respond(HttpStatusCode.OK) },
                                failure = { call.respond(HttpStatusCode.InternalServerError) }
                            )
                    },
                    failure = { call.respond(HttpStatusCode.BadRequest, it.reason.name) }
                )
        }
        post("/like") {
            val validQuestionId = call.parameters["question_id"]?.toIntOrNull()
            validQuestionId.toResultOr {
                QicooError(cndjp.qicoo.api.QicooErrorReason.ParseRequestFailure.withLog())
            }
                .mapBoth(
                    success = { validatedQuestionId ->
                        questionService.incr(validatedQuestionId)
                        call.respond(HttpStatusCode.OK)
                    },
                    failure = { call.respond(HttpStatusCode.BadRequest, it.reason.name) }
                )
        }
        get("/detail") {
            call.respond(HttpStatusCode.OK, "question detail routing ok")
        }
    }
}
