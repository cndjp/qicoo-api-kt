package cndjp.qicoo.api.controller.account

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.api.http_resource.session.account.GitHubSession
import cndjp.qicoo.api.withLog
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.toResultOr
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import org.kodein.di.Kodein

fun Route.accountController(kodein: Kodein) {
    route("/api/v1/") {

        // 基本的に /accountはcokkieに保存した後、JWT認証ヘッダーとしてやり取りする。
        // 認証はパスワードかtwitterで設定できるようにする
        // いずれQuestionControllerもそのヘッダーを使ってuser名を入れて投稿する
        route("/account") {

            get {
                val session = call.sessions.get<GitHubSession>()
                session.toResultOr {
                    QicooError.AuthorizationFailure.withLog()
                }
                    .mapBoth(
                        success = { TODO() },
                        failure = { call.respond(HttpStatusCode.BadRequest, it.name) }
                    )
                TODO() // アカウント情報を取得する。認証ヘッダーを復号化して対応したUserResponseクラスとかを返す
            }

            post("/login") {
                val session = call.sessions.get<GitHubSession>()
                session.toResultOr {
                    QicooError.AuthorizationFailure.withLog()
                }
                    .mapBoth(
                        success = { call.respond(HttpStatusCode.OK) },
                        failure = { call.respond(HttpStatusCode.BadRequest, it.name) }
                    )
            }
        }
    }
}
