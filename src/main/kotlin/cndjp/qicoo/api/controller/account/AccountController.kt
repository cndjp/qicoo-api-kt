package cndjp.qicoo.api.controller.account

import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.kodein.di.Kodein

fun Route.accountController(kodein: Kodein) {
    route("/api/v1/") {

        // 基本的に /accountはcokkieに保存した後、JWT認証ヘッダーとしてやり取りする。
        // 認証はパスワードかtwitterで設定できるようにする
        // いずれQuestionControllerもそのヘッダーを使ってuser名を入れて投稿する
        route("/account") {

            get {
                TODO() // アカウント情報を取得する。認証ヘッダーを復号化して対応したUserResponseクラスとかを返す
            }

            post("/signup") {
                TODO() // サインアップする。こパスワードとかtwitterの認証情報を照会して、
                       // user idだけど、JWTとかでやりとりしたい
            }

            post("/login") {
                TODO() // パスワードとかtwitterの認証情報を照会して、
                       // OKならJWTとかで認証ヘッダーをつけて、NGなら4XX 5XXを返す。
            }
        }
    }
}