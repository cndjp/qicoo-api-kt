package cndjp.qicoo.api.controller.healthcheck

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route

fun Route.healthCheckController() {

    route("/hc") {
        get {
            call.respondText { "ok" }
        }
    }
}