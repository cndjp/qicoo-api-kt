package cndjp.qicoo.controller.question

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.questionController() {

    route("/question") {
        get {
            call.respondText { "question routing ok" }
        }
        get("/detail") {
            call.respond(HttpStatusCode.OK, "question detail routing ok")
        }
    }
}