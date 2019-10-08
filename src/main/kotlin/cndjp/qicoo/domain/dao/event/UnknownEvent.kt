package cndjp.qicoo.domain.dao.event

import cndjp.qicoo.domain.model.event.event
import cndjp.qicoo.utils.getNowDateTimeJst
import cndjp.qicoo.utils.toDateTimeJst
import org.jetbrains.exposed.sql.select

// とても不服な実装だが、questionのinsert等で見つからない場合に常にデフォルトで参照させるダミーをいれておく
// 実装自体に関してはとても上手いと思ってる。
// まずlazy valなのでこの変数は一度呼ばれた時のみ実行される。
// 中身はselectでunknownNameを持ってくる。あったらそれを変数にいれておわり。
// ここから面白く、?オペレータでもしnullがselectで返ってきた場合のみ、NewXXXX.newによってinsertする。
// これにより常に1個のunknownのデータが存在することとなる。
val unknownEvent by lazy {
    val unknownName = "unknown event"
    event.select { event.name eq unknownName }.map { it.toEvent() }.firstOrNull()
        ?: NewEvent.new {
                name = unknownName
                start_at = "1970-01-01 19:00:00".toDateTimeJst()
                end_at = "1970-01-01 19:00:00".toDateTimeJst()
                created = getNowDateTimeJst()
                updated = getNowDateTimeJst()
            }.toEvent()
}
