package cndjp.qicoo.api.controller.event

import io.ktor.routing.Route
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.put
import io.ktor.routing.route
import org.kodein.di.Kodein

fun Route.eventController(kodein: Kodein) {
    route("/api/v1/") {

        // 基本的に /eventと/programは勉強会登録およびプログラムの登録を行う。
        // APIとしては公開するが、個人的な想定としてはここにコマンドラインなどプログラムを通じてイベントの登録作業を行うつもり
        // データ構造が肝と思われる。
        // 余力があればAccountControllerと連携して、Adminユーザだけリクエストを受けるみたいなことをしていいかもしれない

        route("/event/{event_id}") {

            get {
                TODO() // EventResponseクラスとかを返す。
            }

            post {
                TODO() // JSONを受けてイベントを登録する。
            }

            put {
                TODO() // JSONを受けてイベントを更新する。
            }

            route("/program/{program_id}") {

                get {
                    TODO() // ProgramResponseクラスとかを返す。
                }

                post {
                    TODO() // JSONを受けてプログラムを登録する。実はこのエンドポイントが肝。おそらくJSONのリストでプログラムを登録するが、イベントIDと紐づけてかつそれぞれの登録プログラムの開始と終了時間が整合性が取れている必要がある
                }

                put {
                    TODO() // JSONを受けてプログラムを更新する。↑で使うであろう時間系のバリデーションは引き継いでいいのかもしれない。
                }

            }
        }
    }
}
