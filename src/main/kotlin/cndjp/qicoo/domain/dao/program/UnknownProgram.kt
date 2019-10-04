package domain.dao.program

import domain.dao.event.unknownEvent
import domain.model.program.program
import org.jetbrains.exposed.sql.select
import utils.getNowDateTimeJst
import utils.toDateTimeJst

// とても不服な実装だが、questionのinsert等で見つからない場合に常にデフォルトで参照させるダミーをいれておく
// 実装自体に関してはとても上手いと思ってる。
// まずlazy valなのでこの変数は一度呼ばれた時のみ実行される。
// 中身はselectでunknownNameを持ってくる。あったらそれを変数にいれておわり。
// ここから面白く、?オペレータでもしnullがselectで返ってきた場合のみ、NewXXXX.newによってinsertする。
// これにより常に1個のunknownのデータが存在することとなる。
val unknownProgram by lazy {
    val unknownName = "unknown program"
    program.select { program.name eq unknownName }.map { it.toProgram() }.firstOrNull()
        ?: NewProgram.new {
            name = unknownName
            event_id = unknownEvent.id
            start_at = "1970-01-01 19:00:00".toDateTimeJst()
            end_at = "1970-01-01 19:00:00".toDateTimeJst()
            created = getNowDateTimeJst()
            updated = getNowDateTimeJst()
        }.toProgram()
}
