package cndjp.qicoo.domain.repository.reply

import cndjp.qicoo.api.QicooError
import cndjp.qicoo.domain.dao.reply.ReplyList
import com.github.michaelbull.result.Result

interface ReplyRepository {
    fun add(id: Int, comment: String): Result<Unit, QicooError>
    fun findById(id: Int): ReplyList
}